package com.messenger.events;

import com.messenger.database.pojo.WebSocketIncomingMessage;

/**
 * Simple {@link org.greenrobot.eventbus.EventBus} message event to
 * handle incoming message in activity.
 *
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
