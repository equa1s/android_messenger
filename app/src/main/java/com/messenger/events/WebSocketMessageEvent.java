package com.messenger.events;

import com.messenger.database.pojo.IWebSocketData;
import com.messenger.database.pojo.WebSocketGetMessages;

/**
 *
 * @author equals on 13.12.16.
 */
public class WebSocketMessageEvent implements MessageEvent {

    private WebSocketGetMessages webSocketGetMessages;

    public WebSocketMessageEvent(WebSocketGetMessages webSocketGetMessages) {
        this.webSocketGetMessages = webSocketGetMessages;
    }

    @Override
    public IWebSocketData getMessage() {
        return webSocketGetMessages;
    }

}
