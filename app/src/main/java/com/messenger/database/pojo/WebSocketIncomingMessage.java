package com.messenger.database.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Simple incoming web socket message entity.
 *
 * @author equals on 16.11.16.
 */
public class WebSocketIncomingMessage implements IWebSocketData {

    @Expose @SerializedName("body") private String body;
    @Expose @SerializedName("sender") private String sender;
    @Expose @SerializedName("sent_date") private Long dateSent;
    @Expose @SerializedName("received_date") private Long dateReceived;

    private WebSocketIncomingMessage(Builder builder) {
        body = builder.body;
        sender = builder.sender;
        dateSent = builder.dateSent;
        dateReceived = builder.dateReceived;
    }

    public String getBody() {
        return body;
    }

    public String getSender() {
        return sender;
    }

    public Long getDateSent() {
        return dateSent;
    }

    public Long getDateReceived() {
        return dateReceived;
    }

    @Override
    public String toString() {
        return "\nWebSocketIncomingMessage { " +
                "\nbody='" + body + '\'' +
                ", \n\t\tsender='" + sender + '\'' +
                ", \n\t\tdateSent=" + dateSent +
                ", \n\t\tdateReceived=" + dateReceived +
                "\n}";
    }

    public static final class Builder {

        @Expose @SerializedName("body") private String body;
        @Expose @SerializedName("sender") private String sender;
        @Expose @SerializedName("sent_date") private Long dateSent;
        @Expose @SerializedName("received_date") private Long dateReceived;

        public Builder() {
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder sender(String sender) {
            this.sender = sender;
            return this;
        }

        public Builder dateSent(Long dateSent) {
            this.dateSent = dateSent;
            return this;
        }

        public Builder dateReceived(Long dateReceived) {
            this.dateReceived = dateReceived;
            return this;
        }

        public WebSocketIncomingMessage build() {
            return new WebSocketIncomingMessage(this);
        }
    }
}
