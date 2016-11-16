package com.messenger.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * TODO: -create one to many, -add additional fields, -create table of threads (NOT MESSAGES)
 *
 * @author equals on 15.11.16.
 */

@Entity(nameInDb = "messages")
public class MessageEntity {

    public static final int OUTGOING = 0;
    public static final int INCOMING = 1;

    private long id;
    private long threadId;
    private String body;
    private String from;
    private long receivedTime;
    private long sentTime;
    private int type;

    public boolean isOutgoing() {
        return type == OUTGOING;
    }

    public boolean isIncoming() {
        return type == INCOMING;
    }

    private MessageEntity(Builder builder) {
        id = builder.id;
        body = builder.body;
        from = builder.from;
        receivedTime = builder.receivedTime;
        sentTime = builder.sentTime;
        type = builder.type;
    }

    @Generated(hash = 655175726)
    public MessageEntity(long id, long threadId, String body, String from, long receivedTime, long sentTime, int type) {
        this.id = id;
        this.threadId = threadId;
        this.body = body;
        this.from = from;
        this.receivedTime = receivedTime;
        this.sentTime = sentTime;
        this.type = type;
    }

    @Generated(hash = 1797882234)
    public MessageEntity() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getThreadId() {
        return this.threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getReceivedTime() {
        return this.receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public long getSentTime() {
        return this.sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }


    /**
     * {@code MessageEntity} builder static inner class.
     */
    public static final class Builder {
        private long id;
        private String body;
        private String from;
        private long receivedTime;
        private long sentTime;
        private int type;

        private Builder() {
        }

        /**
         * Sets the {@code id} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param id the {@code id} to set
         * @return a reference to this Builder
         */
        public Builder id(long id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the {@code body} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param body the {@code body} to set
         * @return a reference to this Builder
         */
        public Builder body(String body) {
            this.body = body;
            return this;
        }

        /**
         * Sets the {@code from} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param from the {@code from} to set
         * @return a reference to this Builder
         */
        public Builder from(String from) {
            this.from = from;
            return this;
        }

        /**
         * Sets the {@code receivedTime} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param receivedTime the {@code receivedTime} to set
         * @return a reference to this Builder
         */
        public Builder receivedTime(long receivedTime) {
            this.receivedTime = receivedTime;
            return this;
        }

        /**
         * Sets the {@code sentTime} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param sentTime the {@code sentTime} to set
         * @return a reference to this Builder
         */
        public Builder sentTime(long sentTime) {
            this.sentTime = sentTime;
            return this;
        }

        /**
         * Sets the {@code type} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param type the {@code type} to set
         * @return a reference to this Builder
         */
        public Builder type(int type) {
            this.type = type;
            return this;
        }

        /**
         * Returns a {@code MessageEntity} built from the parameters previously set.
         *
         * @return a {@code MessageEntity} built with parameters of this {@code MessageEntity.Builder}
         */
        public MessageEntity build() {
            return new MessageEntity(this);
        }
    }
}
