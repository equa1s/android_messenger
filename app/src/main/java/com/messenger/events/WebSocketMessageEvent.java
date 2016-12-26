package com.messenger.events;

import com.messenger.database.pojo.IWebSocketData;
import com.messenger.database.pojo.WebSocketMessages;

/**
 * Simple {@link org.greenrobot.eventbus.EventBus} message event to
 * handle incoming pool of messages in activity.
 *
 * @author equals on 13.12.16.
 */
public class WebSocketMessageEvent implements MessageEvent {

    private WebSocketMessages webSocketMessages;

    public WebSocketMessageEvent(WebSocketMessages webSocketMessages) {
        this.webSocketMessages = webSocketMessages;
    }

    @Override
    public IWebSocketData getMessage() {
        return webSocketMessages;
    }

}
