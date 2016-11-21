package com.messenger.database.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author equals on 16.11.16.
 */
public class WebSocketOutgoingMessage implements WebSocketMessage {

    @JsonProperty("body") private String body;
    @JsonProperty("recipient") private String recipient;
    @JsonProperty("sent_date") private long dateSent;
    @JsonProperty("received_date") private long dateReceived;

    private WebSocketOutgoingMessage(Builder builder) {
        body = builder.body;
        recipient = builder.recipient;
        dateSent = builder.dateSent;
        dateReceived = builder.dateReceived;
    }

    public String getBody() {
        return body;
    }

    public String getRecipient() {
        return recipient;
    }

    public long getDateSent() {
        return dateSent;
    }

    public long getDateReceived() {
        return dateReceived;
    }

    public static final class Builder {
        private String body;
        private String recipient;
        private long dateSent;
        private long dateReceived;

        public Builder() {
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder recipient(String recipient) {
            this.recipient = recipient;
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

        public WebSocketOutgoingMessage build() {
            return new WebSocketOutgoingMessage(this);
        }
    }
}
