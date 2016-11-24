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
 * Wrapper for web socket connection
 *
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

        String wsUrl = BuildConfig.MESSENGER_URL.replaceAll("http", "ws");

        Request request = new Request.Builder()
                .url(wsUrl)
                .build();

        WebSocketCall.create(client, request).enqueue(this);
    }

    public void sendMessage(RequestBody requestBody) throws IOException {
        webSocket.sendMessage(requestBody);
    }

    @Override
    public void onOpen(final WebSocket webSocket, Response response) {
        this.webSocket = webSocket;
        try {
            Log.d(TAG, "onOpen: "  +response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // if (mWebSocketMessageReceiver != null) mWebSocketMessageReceiver.onOpen(webSocket, response);
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
        Log.d(TAG, "onClose: " + code + ", reason: " + reason);
    }

    interface WebSocketMessageReceiver {
//        void onOpen(final WebSocket webSocket, Response response);
        void onMessage(WebSocketIncomingMessage webSocketIncomingMessage);
//        void onClose(int code, String reason);
//        void onFailure(IOException e, Response response);
    }
}
