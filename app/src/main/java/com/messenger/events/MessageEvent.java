package com.messenger.events;


import com.messenger.database.pojo.WebSocketMessage;

/**
 * @author equals on 18.11.16.
 */
public interface MessageEvent {
    WebSocketMessage getMessage();
}
