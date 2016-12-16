package com.messenger.service;

import android.content.Context;
import android.util.Log;

import com.messenger.BuildConfig;
import com.messenger.database.pojo.WebSocketGetMessages;
import com.messenger.database.pojo.WebSocketIncomingMessage;
import com.messenger.database.pojo.WebSocketMessage;
import com.messenger.preferences.MessengerSharedPreferences;
import com.messenger.util.Base64;
import com.messenger.util.GsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * Wrapper for OkHttp web socket.
 *
 * @author equals on 17.11.16.
 */
public class WebSocketConnection implements WebSocketListener {

    private static final String TAG = WebSocketConnection.class.getSimpleName();

    private static final String ENDPOINT = "/v1/websocket";

    private WebSocketMessageReceiver mWebSocketMessageReceiver;
    private WebSocket mWebSocket;

    WebSocketConnection(Context context) {
        this.mWebSocketMessageReceiver = (WebSocketMessageReceiver) context;

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        String wsUrl = BuildConfig.MESSENGER_URL
                .replaceAll("http", "ws");

        Request request = new Request.Builder()
                .url(wsUrl + ENDPOINT)
                .header("Authorization", "Basic " + Base64.encodeBytes((MessengerSharedPreferences.getUserLogin(context) + ":" + MessengerSharedPreferences.getUserPassword(context)).getBytes()))
                .build();

        WebSocketCall.create(client, request).enqueue(this);
    }

    @Override
    public void onOpen(final WebSocket webSocket, Response response) {
        this.mWebSocket = webSocket;
        try {
            mWebSocket.sendMessage(RequestBody.create(WebSocket.TEXT, GsonUtils.toJson(new WebSocketMessage.Builder()
                    .data(null)
                    .count(null)
                    .method(Method.GET)
                    .build())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        closeResponseBody(response);
    }

    @Override
    public void onFailure(IOException e, Response response) {
        if (mWebSocketMessageReceiver != null) mWebSocketMessageReceiver.onFailure(e, response);
    }

    @Override
    public void onMessage(ResponseBody message) throws IOException {
        String response = message.string();
            message.close();
        Log.d(TAG, "Got message from server: " + response);

        WebSocketGetMessages webSocketGetMessages = GsonUtils.fromJson(response, WebSocketGetMessages.class);
        Log.d(TAG, "Message: " + webSocketGetMessages.toString());

        if (mWebSocketMessageReceiver != null) mWebSocketMessageReceiver.onMessage(webSocketGetMessages);

        Log.d(TAG, "Response: " + webSocketGetMessages);
        sendResponse(webSocketGetMessages);
    }

    @Override
    public void onPong(Buffer payload) {
        Log.d(TAG, "Pong: " + payload.readUtf8());
    }

    @Override
    public void onClose(int code, String reason) {
        Log.d(TAG, "onClose: " + code + ", reason: " + reason);
        if (mWebSocketMessageReceiver != null) mWebSocketMessageReceiver.onClose(code, reason);
    }

    /**
     * Send response to server when client received messages.
     *  Response: {
     *              "method":"/read",
     *              "body":[1481718508000, 1481718531000, ... ]
     *            }
     *
     * @param webSocketGetMessages List of received messages from server
     * @throws IOException
     */
    private void sendResponse(WebSocketGetMessages webSocketGetMessages) throws IOException {
        List<WebSocketIncomingMessage> messages = webSocketGetMessages.getMessages();
        if (messages != null && !messages.isEmpty()) {

            Map<String, Object> response = new HashMap<>();
            List<Long> timestamps = new ArrayList<>();

                for (WebSocketIncomingMessage msg : messages) {
                    timestamps.add(msg.getDateReceived());
                }

            response.put("method", Method.READ);
            response.put("body", timestamps);

            send(RequestBody.create(WebSocket.TEXT, GsonUtils.toJson(response)));
        }
    }

    /**
     * Sends message through web socket.
     *
     * @param requestBody RequestBody with message json object {@link com.messenger.database.pojo.WebSocketOutgoingMessage} that will be sent
     * @throws IOException
     */
    public void send(RequestBody requestBody) throws IOException {
        if (mWebSocket != null) {
            mWebSocket.sendMessage(requestBody);
        }
    }

    /**
     * Close web socket connection.
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        if (mWebSocket != null) {
            try {
                mWebSocket.close(1000, "OK");
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * We should close response body.
     * See issue on <a>
     *     https://github.com/square/okhttp/issues/2303#issuecomment-187696928
     * </a>
     *
     * Reason: A connection to http://equals.s1z.info
     *      was leaked. Did you forget to disconnect a response body?
     *
     * @param response Response which body will be closed.
     */
    private void closeResponseBody(Response response) {
        if (response != null) {
            ResponseBody body = response.body();
            if (body != null) {
                body.close();
            }
        }
    }

    /** Listener for server-initiated messages on a connected {@link WebSocketConnection}. */
    interface WebSocketMessageReceiver {
        /**
         * Called when the request has successfully been upgraded to a web socket. This method is called
         * on the message reading thread to allow setting up any state before the {@linkplain #onMessage
         * message}, {@linkplain #onPong pong}, and {@link #onClose disconnect} callbacks start.
         *
         * <p><b>Do not</b> use this callback to write to the web socket. Start a new thread or use
         * another thread in your application.
         */
        void onOpen(final WebSocket mWebSocket, Response response);

        /**
         * Called when a server message is received. The {@code type} indicates whether the {@code
         * payload} should be interpreted as UTF-8 text or binary data.
         *
         * <p>Implementations <strong>must</strong> call {@code source.disconnect()} before returning. This
         * indicates completion of parsing the message payload and will consume any remaining bytes in the
         * message.
         *
         * <p>The {@linkplain ResponseBody#contentType() content type} of {@code message} will be either
         * {@link WebSocket#TEXT} or {@link WebSocket#BINARY} which indicates the format of the message.
         */
        void onMessage(WebSocketGetMessages webSocketGetMessages);

        /**
         * Called when the server sends a disconnect message. This may have been initiated from a call to
         * {@link WebSocket#close(int, String) disconnect()} or as an unprompted message from the server.
         *
         * @param code The <a href="http://tools.ietf.org/html/rfc6455#section-7.4.1">RFC-compliant</a>
         * status code.
         * @param reason Reason for disconnect or an empty string.
         */
        void onClose(int code, String reason);

        /**
         * Called when the transport or protocol layer of this web socket errors during communication.
         *
         * @param response Present when the failure is a direct result of the response (e.g., failed
         * upgrade, non-101 response code, etc.). {@code null} otherwise.
         */
        void onFailure(IOException e, Response response);
    }

    public interface Method {
        String GET = "/get";
        String SEND = "/send";
        String READ = "/read";
    }
}
