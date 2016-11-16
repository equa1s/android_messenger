package com.messenger.database.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author equals on 16.11.16.
 */
public class WebSocketMessage {

    @JsonProperty("body") private String body;
    @JsonProperty("sender") private String sender;
    @JsonProperty("sent_date") private long dateSent;
    @JsonProperty("received_date") private long dateReceived;

    private WebSocketMessage(Builder builder) {
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

    public long getDateSent() {
        return dateSent;
    }

    public long getDateReceived() {
        return dateReceived;
    }

    public static final class Builder {

        private String body;
        private String sender;
        private long dateSent;
        private long dateReceived;

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

        public Builder dateSent(long dateSent) {
            this.dateSent = dateSent;
            return this;
        }

        public Builder dateReceived(long dateReceived) {
            this.dateReceived = dateReceived;
            return this;
        }

        public WebSocketMessage build() {
            return new WebSocketMessage(this);
        }
    }
}
