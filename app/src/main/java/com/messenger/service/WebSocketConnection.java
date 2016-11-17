package com.messenger.service;


import com.messenger.database.pojo.WebSocketMessage;
import com.messenger.util.JsonUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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

    private WebSocketMessageListener webSocketMessageListener;
    private WebSocket webSocket;

    public WebSocketConnection(WebSocketMessageListener webSocketMessageListener) {
        this.webSocketMessageListener = webSocketMessageListener;

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0,  TimeUnit.MILLISECONDS)
                .build();

        // TODO: set web socket url
        Request request = new Request.Builder()
                .url("web socket url")
                .build();

        WebSocketCall.create(client, request).enqueue(this);

        // TODO: shutdown web socket
        // client.dispatcher().executorService().shutdown();
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    @Override
    public void onOpen(final WebSocket webSocket, Response response) {
        this.webSocket = webSocket;
//        if (webSocketMessageListener != null) webSocketMessageListener.onOpen(webSocket, response);
    }

    @Override
    public void onFailure(IOException e, Response response) {
//        if (webSocketMessageListener != null) webSocketMessageListener.onFailure(e, response);
    }

    @Override
    public void onMessage(ResponseBody message) throws IOException {
//        if (webSocketMessageListener != null) webSocketMessageListener.onMessage(JsonUtils.fromJson(message.bytes(), WebSocketMessage.class));
    }

    @Override
    public void onPong(Buffer payload) {
//        if (webSocketMessageListener != null) webSocketMessageListener.onPong(payload);
    }

    @Override
    public void onClose(int code, String reason) {
        if (webSocketMessageListener != null) webSocketMessageListener.onClose(code, reason);
    }

    interface WebSocketMessageListener {
        void onMessage(WebSocketMessage webSocketMessage);
        void onClose(int code, String reason);
//        void onPong(Buffer payload);
//        void onFailure(IOException e, Response response);
//        void onOpen(final WebSocket webSocket, Response response);
    }
}
