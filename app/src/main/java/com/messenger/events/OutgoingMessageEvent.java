package com.messenger.events;

import com.messenger.database.pojo.WebSocketOutgoingMessage;

/**
 *
 * @author equals on 16.11.16.
 */
public class OutgoingMessageEvent implements MessageEvent {

    private WebSocketOutgoingMessage webSocketMessage;

    public OutgoingMessageEvent(WebSocketOutgoingMessage webSocketMessage) {
        this.webSocketMessage = webSocketMessage;
    }

    @Override
    public WebSocketOutgoingMessage getMessage() {
        return webSocketMessage;
    }

}
