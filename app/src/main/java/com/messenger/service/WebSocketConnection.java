package com.messenger.service;


import android.util.Log;

import com.messenger.BuildConfig;
import com.messenger.database.pojo.WebSocketIncomingMessage;
import com.messenger.util.JsonUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;

/**
 * @author equals on 17.11.16.
 */
public class WebSocketConnection implements WebSocketListener {

    private static final String TAG = WebSocketConnection.class.getSimpleName();
    private WebSocketMessageReceiver mWebSocketMessageReceiver;
    private WebSocket webSocket;

    WebSocketConnection(WebSocketMessageReceiver mWebSocketMessageReceiver) {
        this.mWebSocketMessageReceiver = mWebSocketMessageReceiver;

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        // TODO: set web socket url
        Request request = new Request.Builder()
                .url(BuildConfig.MESSENGER_URL + "/ws")
                .build();

        WebSocketCall.create(client, request).enqueue(this);
    }

    public void sendMessage(RequestBody requestBody) throws IOException {
        webSocket.sendMessage(requestBody);
    }

    @Override
    public void onOpen(final WebSocket webSocket, Response response) {
        this.webSocket = webSocket;
    }

    @Override
    public void onFailure(IOException e, Response response) {
        Log.d(TAG, "Failed: " + e.getMessage());
    }

    @Override
    public void onMessage(ResponseBody message) throws IOException {
        Log.d(TAG, "Received message: " + message.string());
        if (mWebSocketMessageReceiver != null) mWebSocketMessageReceiver.onMessage(JsonUtils.fromJson(message.bytes(), WebSocketIncomingMessage.class));

        // INFO : See issue on > https://github.com/square/okhttp/issues/2303#issuecomment-187696928
        message.close();
    }

    @Override
    public void onPong(Buffer payload) {
        Log.d(TAG, "Pong: " + payload.readUtf8());
    }

    @Override
    public void onClose(int code, String reason) {
        try {
            webSocket.close(code, reason);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    interface WebSocketMessageReceiver {
//        void onOpen(final WebSocket webSocket, Response response);
        void onMessage(WebSocketIncomingMessage webSocketIncomingMessage);
//        void onClose(int code, String reason);
//        void onFailure(IOException e, Response response);
    }
}
