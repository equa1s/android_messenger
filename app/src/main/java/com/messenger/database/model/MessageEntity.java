package com.messenger.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * {@link MessageEntity} represents message entity
 * @author equals on 15.11.16.
 */

@Entity(nameInDb = "messages")
public class MessageEntity {

    public static final int OUTGOING = 0;
    public static final int INCOMING = 1;

    @Id(autoincrement = true)
    @Property(nameInDb = "message_id")
    private long id;

    @Property(nameInDb = "thread_id")
    private long threadId;

    private String body;

    /**
     * @param from keeps a sender of message
     */
    private String from;

    @Property(nameInDb = "received_time")
    private long receivedTime;

    @Property(nameInDb = "sent_time")
    private long sentTime;

    private int type;

    public boolean isOutgoing() {
        return type == OUTGOING;
    }

    public boolean isIncoming() {
        return type == INCOMING;
    }

    private MessageEntity(Builder builder) {
        setId(builder.id);
        setThreadId(builder.threadId);
        setBody(builder.body);
        setFrom(builder.from);
        setReceivedTime(builder.receivedTime);
        setSentTime(builder.sentTime);
        setType(builder.type);
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


    public static final class Builder {
        private long id;
        private long threadId;
        private String body;
        private String from;
        private long receivedTime;
        private long sentTime;
        private int type;

        public Builder() {
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder threadId(long threadId) {
            this.threadId = threadId;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder receivedTime(long receivedTime) {
            this.receivedTime = receivedTime;
            return this;
        }

        public Builder sentTime(long sentTime) {
            this.sentTime = sentTime;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public MessageEntity build() {
            return new MessageEntity(this);
        }
    }
}
