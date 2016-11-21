package com.messenger.events;

import com.messenger.database.pojo.WebSocketIncomingMessage;

/**
 * @author equals on 16.11.16.
 */
public class IncomingMessageEvent implements MessageEvent {

    private WebSocketIncomingMessage webSocketMessage;

    public IncomingMessageEvent(WebSocketIncomingMessage webSocketMessage) {
        this.webSocketMessage = webSocketMessage;
    }

    @Override
    public WebSocketIncomingMessage getMessage() {
        return webSocketMessage;
    }

}
