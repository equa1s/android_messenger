package com.messenger.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

import com.messenger.ApplicationContext;
import com.messenger.database.MessengerDatabaseHelper;
import com.messenger.database.model.MessageEntity;
import com.messenger.database.model.ThreadEntity;
import com.messenger.database.pojo.WebSocketGetMessages;
import com.messenger.database.pojo.WebSocketMessage;
import com.messenger.database.pojo.WebSocketOutgoingMessage;
import com.messenger.events.MessageEvent;
import com.messenger.events.OutgoingMessageEvent;
import com.messenger.events.WebSocketMessageEvent;
import com.messenger.notifications.MessageNotifier;
import com.messenger.notifications.NotificationManager;
import com.messenger.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ws.WebSocket;

/**
 * Message service to handle sending/receiving messages
 *  through {@link WebSocket}.
 *
 * @author equals on 16.11.16.
 */
public class MessageService
        extends Service
         implements WebSocketConnection.WebSocketMessageReceiver {

    private static final String TAG = MessageService.class.getSimpleName();

    public static final int SEND_MESSAGE_ACTION = 0;

    private WebSocketConnection webSocketConnection;
    private Messenger mMessenger;

    @Override
    public void onCreate() {
        super.onCreate();
        webSocketConnection = new WebSocketConnection(this);
        mMessenger = new Messenger(new MessageServiceHandler(this));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();
    }

    @Override
    public void onOpen(WebSocket mWebSocket, Response response) {

    }

    @Override
    public void onFailure(IOException e, Response response) {

        Log.d(TAG, "Failed: " + e.getMessage());

        disconnect();

        // info: if web socket was failed then try to re-create it
        webSocketConnection = new WebSocketConnection(this);
    }

    @Override
    public void onMessage(WebSocketGetMessages webSocketGetMessages) {
        handleIncomingMessages(webSocketGetMessages);
    }

    @Override
    public void onClose(int code, String reason) {
        disconnect();
    }

    /**
     * Disconnect web socket connection.
     */
    private void disconnect() {
        if (webSocketConnection != null) {
            try {
                webSocketConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns instance of database helper {@link MessengerDatabaseHelper}.
     *
     * @return Instance of message database.
     */
    protected MessengerDatabaseHelper getMessengerDatabaseHelper() {
        return ((ApplicationContext)getApplication()).getMessengerDatabaseHelper();
    }

    /**
     * Handles incoming messages.
     *
     * @param webSocketGetMessage Incoming messages from web socket
     */
    private void handleIncomingMessages(WebSocketGetMessages webSocketGetMessage) {

        List<MessageEntity> messages = getMessengerDatabaseHelper()
                .readIncomingMessages(webSocketGetMessage);

        if (messages != null) {
            for (MessageEntity messageEntity : messages) {

                getMessengerDatabaseHelper()
                        .getMessageEntityDao()
                        .insert(messageEntity);

                long threadId = messageEntity.getThreadId();

                ThreadEntity staleThreadEntity = getMessengerDatabaseHelper()
                        .getThreadEntityDao()
                        .load(threadId);

                // update message for stale thread entity
                staleThreadEntity.setLastMessage(messageEntity.getBody());

                getMessengerDatabaseHelper()
                        .getThreadEntityDao()
                        .update(staleThreadEntity);

                if (MessageNotifier.isVisibleThread(threadId)) {
                    sendEvent(new WebSocketMessageEvent(webSocketGetMessage));
                } else {
                    NotificationManager.showNotification(this, messageEntity.getFrom(), messageEntity.getBody());
                }
            }
        }

    }

    /**
     * Broadcasts message's events to subscribers.
     *
     * @param messageEvent Simple message event for subscribers.
     */
    private void sendEvent(MessageEvent messageEvent) {
        EventBus.getDefault().post(messageEvent);
    }

    /**
     * Send message to another recipient.
     *
     * @param webSocketMessage Outgoing message
     * @throws IOException
     */
    public void handleOutgoingMessage(WebSocketMessage webSocketMessage) throws IOException {

        // INFO : parse web socket message to MessageEntity
        MessageEntity messageEntity = getMessengerDatabaseHelper()
                .readOutgoingMessage(webSocketMessage);

        // INFO : insert message to messages table
        getMessengerDatabaseHelper().insertMessage(messageEntity);

        // INFO : update thread
        getMessengerDatabaseHelper().updateThread(messageEntity);

        String outgoingMessage = GsonUtils.toJson(webSocketMessage);

        Log.d(TAG, "Outgoing message: " + outgoingMessage);

        if (outgoingMessage != null) {
            webSocketConnection.send(RequestBody.create(WebSocket.TEXT, outgoingMessage));
        }

        sendEvent(new OutgoingMessageEvent((WebSocketOutgoingMessage) webSocketMessage.getWebSocketData()));
    }

    /**
     * Service handler to communicate with UI thread {@link com.messenger.ConversationActivity}.
     */
    private static class MessageServiceHandler extends Handler {

        private final WeakReference<MessageService> mMessageService;

        MessageServiceHandler(MessageService mMessageService) {
            this.mMessageService = new WeakReference<>(mMessageService);
        }

        @Override
        public void handleMessage(Message msg) {
            MessageService mMessageService = this.mMessageService.get();
            if (msg.what == SEND_MESSAGE_ACTION) {
                try {
                    mMessageService.handleOutgoingMessage((WebSocketMessage) msg.obj);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                super.handleMessage(msg);
            }

        }
    }

}
