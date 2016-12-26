package com.messenger.events;

import com.messenger.database.pojo.WebSocketOutgoingMessage;

/**
 * Simple {@link org.greenrobot.eventbus.EventBus} message event to
 * handle outgoing message in activity.
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
