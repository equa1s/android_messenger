package com.messenger.events;

import com.messenger.database.pojo.WebSocketMessage;

/**
 * @author equals on 16.11.16.
 */
public class MessageEvent {

    private WebSocketMessage webSocketMessage;

    public MessageEvent(WebSocketMessage webSocketMessage) {
        this.webSocketMessage = webSocketMessage;
    }

    public WebSocketMessage getWebSocketMessage() {
        return webSocketMessage;
    }

}
