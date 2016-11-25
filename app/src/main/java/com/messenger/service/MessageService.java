package com.messenger.service;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;

import com.messenger.database.MessengerDatabaseHelper;
import com.messenger.database.model.MessageEntity;
import com.messenger.database.model.ThreadEntity;
import com.messenger.database.pojo.WebSocketIncomingMessage;
import com.messenger.database.pojo.WebSocketOutgoingMessage;
import com.messenger.events.IncomingMessageEvent;
import com.messenger.events.MessageEvent;
import com.messenger.events.OutgoingMessageEvent;
import com.messenger.notifications.MessageNotifier;
import com.messenger.notifications.NotificationManager;
import com.messenger.util.JsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.RequestBody;
import okhttp3.ws.WebSocket;

/**
 * @author equals on 16.11.16.
 */
public class MessageService
        extends BaseService
         implements WebSocketConnection.WebSocketMessageReceiver {

    private static final String TAG = MessageService.class.getSimpleName();

    public static final int SEND_MESSAGE_ACTION = 0;

    private WebSocketConnection webSocketConnection;
    private Messenger mMessenger;

    @Override
    public void onCreate(MessengerDatabaseHelper mMessengerDatabaseHelper) {
        super.onCreate();
        webSocketConnection = new WebSocketConnection(this);
        mMessenger = new Messenger(new MessageHandler(this));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    /**
     * Sends message through web connection
     * @param webSocketOutgoingMessage outgoing message to another user
     */
    public void sendMessage(WebSocketOutgoingMessage webSocketOutgoingMessage) throws IOException {

        // INFO : parse web socket message to MessageEntity
        MessageEntity messageEntity = getMessengerDatabaseHelper()
                .readOutgoingMessage(webSocketOutgoingMessage);

        // INFO : insert message to messages table
        getMessengerDatabaseHelper()
                .getMessageEntityDao()
                .insert(messageEntity);

        // INFO : update thread's last message, to show it in conversation fragment like a: login->last_message
        ThreadEntity staleThreadEntity = getMessengerDatabaseHelper()
                .getThreadEntityDao()
                .load(messageEntity.getThreadId());
            staleThreadEntity.setLastMessage(messageEntity.getBody());

        getMessengerDatabaseHelper()
                .getThreadEntityDao()
                .update(staleThreadEntity);

        // INFO : event bus
        sendEvent(new OutgoingMessageEvent(webSocketOutgoingMessage));

        webSocketConnection.sendMessage(RequestBody.create(WebSocket.TEXT, JsonUtils.toJson(webSocketOutgoingMessage)));
    }

    /**
     * Receives a message from another user through {@link WebSocket} connection
     * @param webSocketMessage received from sender
     */
    @Override
    public void onMessage(WebSocketIncomingMessage webSocketMessage) {

        MessageEntity messageEntity = getMessengerDatabaseHelper().readIncomingMessage(webSocketMessage);
        long threadId = messageEntity.getThreadId();

        getMessengerDatabaseHelper()
                .getMessageEntityDao()
                .insert(messageEntity);

        // INFO : load thread
        ThreadEntity staleThreadEntity = getMessengerDatabaseHelper()
                .getThreadEntityDao()
                .load(threadId);

            // update message for stale thread entity
            staleThreadEntity.setLastMessage(messageEntity.getBody());

        getMessengerDatabaseHelper()
                .getThreadEntityDao()
                .update(staleThreadEntity);

        if (MessageNotifier.isVisibleThread(threadId)) {
            sendEvent(new IncomingMessageEvent(webSocketMessage));
        } else {
            NotificationManager.showNotification(this, messageEntity.getFrom(), messageEntity.getBody());
        }

    }

    private void sendEvent(MessageEvent messageEvent) {
        EventBus.getDefault().post(messageEvent);
    }

    private static class MessageHandler extends Handler {

        private final WeakReference<MessageService> mMessageService;

        MessageHandler(MessageService mMessageService) {
            this.mMessageService = new WeakReference<>(mMessageService);
        }

        @Override
        public void handleMessage(Message msg) {
            MessageService mMessageService = this.mMessageService.get();
            switch (msg.what) {

                case SEND_MESSAGE_ACTION:
                    try {
                        mMessageService.sendMessage((WebSocketOutgoingMessage)msg.obj);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    super.handleMessage(msg);
            }

        }
    }

}
