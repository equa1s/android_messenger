package com.messenger.service;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;

import com.messenger.database.model.DaoSession;
import com.messenger.database.model.MessageEntity;
import com.messenger.database.model.ThreadEntity;
import com.messenger.database.model.ThreadEntityDao;
import com.messenger.database.pojo.WebSocketIncomingMessage;
import com.messenger.database.pojo.WebSocketMessage;
import com.messenger.database.pojo.WebSocketOutgoingMessage;
import com.messenger.events.IncomingMessageEvent;
import com.messenger.events.MessageEvent;
import com.messenger.events.OutgoingMessageEvent;
import com.messenger.util.JsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.RequestBody;
import okhttp3.ws.WebSocket;

/**
 * @author equals on 16.11.16.
 */
public class MessageRetrievalService
        extends BaseService
         implements WebSocketConnection.WebSocketMessageReceiver {

    private static final String TAG = MessageRetrievalService.class.getSimpleName();

    public static final int SEND_MESSAGE_ACTION = 0;

    private WebSocketConnection webSocketConnection;
    private Messenger mMessenger;

    @Override
    public void onCreate(DaoSession daoSession) {
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

    /** Sends message through {@link WebSocket} connection
     * @param webSocketOutgoingMessage outgoing message to another user
     */
    public void sendMessage(WebSocketOutgoingMessage webSocketOutgoingMessage) throws IOException {
        MessageEntity messageEntity = new MessageReader(webSocketOutgoingMessage).getOutgoingMessage();
        daoSession().getMessageEntityDao().insert(messageEntity);
        sendEvent(new OutgoingMessageEvent(webSocketOutgoingMessage));
        webSocketConnection.sendMessage(RequestBody.create(WebSocket.TEXT, JsonUtils.toJson(webSocketOutgoingMessage)));
    }

    /**
     * Receives a message from another user through {@link WebSocket} connection
     * @param webSocketMessage received from sender
     */
    @Override
    public void onMessage(WebSocketIncomingMessage webSocketMessage) {
        MessageEntity messageEntity = new MessageReader(webSocketMessage).getIncomingMessageEntity();
        daoSession().getMessageEntityDao().insert(messageEntity);
        sendEvent(new IncomingMessageEvent(webSocketMessage));
    }

    private void sendEvent(MessageEvent messageEvent) {
        EventBus.getDefault().post(messageEvent);
    }

    public class MessageReader {

        private WebSocketMessage webSocketMessage;
        private ThreadEntityDao threadEntityDao;

        MessageReader(WebSocketMessage webSocketMessage) {
            this.webSocketMessage = webSocketMessage;
            this.threadEntityDao = daoSession().getThreadEntityDao();
        }

        MessageEntity getIncomingMessageEntity() {
            WebSocketIncomingMessage incomingMessage = (WebSocketIncomingMessage) webSocketMessage;
            ThreadEntity threadEntity = (ThreadEntity) threadEntityDao.queryRaw("from = ?", incomingMessage.getSender());
            return new MessageEntity.Builder()
                    .body(incomingMessage.getBody())
                    .from(incomingMessage.getSender())
                    .receivedTime(incomingMessage.getDateReceived())
                    .sentTime(incomingMessage.getDateSent())
                    .type(MessageEntity.INCOMING)
                    .threadId(threadEntity.getThreadId())
                    .build();
        }

        MessageEntity getOutgoingMessage() {
            WebSocketOutgoingMessage outgoingMessage = (WebSocketOutgoingMessage) webSocketMessage;
            ThreadEntity threadEntity = (ThreadEntity) threadEntityDao.queryRaw("from = ?", outgoingMessage.getRecipient());
            return new MessageEntity.Builder()
                    .body(outgoingMessage.getBody())
                    .from(outgoingMessage.getRecipient())
                    .receivedTime(outgoingMessage.getDateReceived())
                    .sentTime(outgoingMessage.getDateSent())
                    .type(MessageEntity.OUTGOING)
                    .threadId(threadEntity.getThreadId())
                    .build();
        }

    }

    private static class MessageHandler extends Handler {

        private final WeakReference<MessageRetrievalService> mMessageService;

        MessageHandler(MessageRetrievalService mMessageService) {
            this.mMessageService = new WeakReference<>(mMessageService);
        }

        @Override
        public void handleMessage(Message msg) {
            MessageRetrievalService mMessageService = this.mMessageService.get();
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
