package com.messenger.database.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Simple outgoing web socket message entity.
 *
 * @author equals on 16.11.16.
 */
public class WebSocketOutgoingMessage implements IWebSocketData {

    @Expose @SerializedName("body") private String body;
    @Expose @SerializedName("recipient") private String recipient;
    @Expose @SerializedName("sent_date") private long dateSent;
    @Expose @SerializedName("received_date") private long dateReceived;

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

    @Override
    public String toString() {
        return "\nWebSocketOutgoingMessage { " +
                "\n\tbody='" + body + '\'' +
                ", \n\t\trecipient='" + recipient + '\'' +
                ", \n\t\tdateSent=" + dateSent +
                ", \n\tdateReceived=" + dateReceived +
                "\n}\n";
    }

    public static final class Builder {

        @Expose
        @SerializedName("body") private String body;
        @Expose
        @SerializedName("recipient") private String recipient;
        @Expose
        @SerializedName("sent_date") private long dateSent;
        @Expose
        @SerializedName("received_date") private long dateReceived;

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
