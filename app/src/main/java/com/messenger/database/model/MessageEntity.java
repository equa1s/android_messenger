package com.messenger.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Entity that presents messages table in database.
 *
 * @author equals on 15.11.16.
 */

@Entity(nameInDb = "messages")
public class MessageEntity {

    public static final int OUTGOING = 0;
    public static final int INCOMING = 1;

    @Id(autoincrement = true)
    @Property(nameInDb = "message_id")
    private Long id;

    @Property(nameInDb = "thread_id")
    private Long threadId;

    private String body;
    private String from;

    @Property(nameInDb = "received_time")
    private long receivedTime;

    @Property(nameInDb = "sent_time")
    private long sentTime;

    private int type;

    public MessageEntity() {
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

    @Generated(hash = 1532295514)
    public MessageEntity(Long id, Long threadId, String body, String from,
            long receivedTime, long sentTime, int type) {
        this.id = id;
        this.threadId = threadId;
        this.body = body;
        this.from = from;
        this.receivedTime = receivedTime;
        this.sentTime = sentTime;
        this.type = type;
    }

    public boolean isOutgoing() {
        return type == OUTGOING;
    }

    public boolean isIncoming() {
        return type == INCOMING;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getThreadId() {
        return this.threadId;
    }

    public void setThreadId(Long threadId) {
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

    @Override
    public String toString() {
        return "MessageEntity{" +
                "id=" + id +
                ", threadId=" + threadId +
                ", body='" + body + '\'' +
                ", from='" + from + '\'' +
                ", receivedTime=" + receivedTime +
                ", sentTime=" + sentTime +
                ", type=" + type +
                '}';
    }


    public static final class Builder {

        private Long id;
        private Long threadId;
        private String body;
        private String from;
        private long receivedTime;
        private long sentTime;
        private int type;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder threadId(Long threadId) {
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
