package com.messenger.database.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class presents web socket messages from server pool of
 * messages that will sent to recipient but not delivered
 * because of internet connection.
 *
 * @author equals on 14.12.16.
 */
public class WebSocketMessages implements IWebSocketData {

    @Expose @SerializedName("count") private Integer count;
    @Expose @SerializedName("messages") private List<WebSocketIncomingMessage> messages;

    private WebSocketMessages(Builder builder) {
        count = builder.count;
        messages = builder.messages;
    }

    public Integer getCount() {
        return count;
    }

    public List<WebSocketIncomingMessage> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "\nWebSocketMessages { " +
                "\n\t\tcount=" + count +
                ", \n\t\tmessages=" + messages +
                "\n}";
    }

    public static final class Builder {

        @Expose @SerializedName("count") private Integer count;
        @Expose @SerializedName("messages") private List<WebSocketIncomingMessage> messages;

        public Builder() {
        }

        public Builder count(Integer count) {
            this.count = count;
            return this;
        }

        public Builder messages(List<WebSocketIncomingMessage> messages) {
            this.messages = messages;
            return this;
        }

        public WebSocketMessages build() {
            return new WebSocketMessages(this);
        }
    }

}
