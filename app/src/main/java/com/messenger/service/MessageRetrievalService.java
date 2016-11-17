package com.messenger.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

import com.messenger.database.pojo.WebSocketMessage;
import com.messenger.events.MessageEvent;
import com.messenger.util.JsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ws.WebSocket;
import okio.Buffer;

/**
 * @author equals on 16.11.16.
 *
 * TODO: create web socket connection & start it when user start chatting
 */
public class MessageRetrievalService extends Service implements WebSocketConnection.WebSocketMessageListener {

    private static final String TAG = MessageRetrievalService.class.getSimpleName();

    public static final int SEND_MESSAGE_ACTION = 0;
    public static final int RECEIVE_MESSAGE_ACTION = 1;

    private WebSocketConnection webSocketConnection;
    private final Messenger mMessenger = new Messenger(new MessageHandler(this));

    public MessageRetrievalService() {
        webSocketConnection = new WebSocketConnection(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // TODO: send message to another recipient through {@link WebSocket.java}

    /**
     * Sends message to another user
     * @param webSocketMessage
     */
    public void sendMessage(WebSocketMessage webSocketMessage) throws IOException {
        // TODO: insert SENT message to database
        sendMessage(new MessageEvent(webSocketMessage));
        webSocketConnection.getWebSocket().sendMessage(RequestBody.create(WebSocket.TEXT, JsonUtils.toJson(webSocketMessage)));
    }

    @Override
    public void onMessage(WebSocketMessage webSocketMessage) {
        // TODO: insert RECEIVED message to database
        sendMessage(new MessageEvent(webSocketMessage));
    }

    @Override
    public void onClose(int code, String reason) {

    }

//    @Override
//    public void onPong(Buffer payload) {
//
//    }
//
//    @Override
//    public void onFailure(IOException e, Response response) {
//
//    }
//
//    @Override
//    public void onOpen(final WebSocket webSocket, Response response) {
//        Log.d(TAG, "Open connection...");
//    }

    private void sendMessage(MessageEvent messageEvent) {
        EventBus.getDefault().post(messageEvent);
    }

    private static class MessageHandler extends Handler {

        private final WeakReference<MessageRetrievalService> mMessageService;

        public MessageHandler(MessageRetrievalService mMessageService) {
            this.mMessageService = new WeakReference<>(mMessageService);
        }

        @Override
        public void handleMessage(Message msg) {
            MessageRetrievalService mMessageService = this.mMessageService.get();
            switch (msg.what) {

                case SEND_MESSAGE_ACTION:
                    try {
                        mMessageService.sendMessage((WebSocketMessage)msg.obj);
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
