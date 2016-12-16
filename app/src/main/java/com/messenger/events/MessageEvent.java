package com.messenger.events;


import com.messenger.database.pojo.IWebSocketData;

/**
 *
 * @author equals on 18.11.16.
 */
public interface MessageEvent {
    IWebSocketData getMessage();
}
