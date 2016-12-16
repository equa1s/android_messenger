package com.messenger.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

/**
 * Entity that presents threads table in database.
 *
 * @author equals on 16.11.16.
 */
@Entity(nameInDb = "threads", indexes = {
        @Index(value = "userId", unique = true)
})
public class ThreadEntity {

    @Id(autoincrement = true)
    @Property(nameInDb = "thread_id")
    private Long threadId;

    @Property(nameInDb = "user_login")
    private String userId;

    @Property(nameInDb = "last_message")
    private String lastMessage;

    @Property(nameInDb = "date")
    private Long lastMessageDate;

    public ThreadEntity() {
    }

    private ThreadEntity(Builder builder) {
        setThreadId(builder.threadId);
        setUserId(builder.userId);
        setLastMessage(builder.lastMessage);
        setLastMessageDate(builder.lastMessageDate);
    }

    @Generated(hash = 261413255)
    public ThreadEntity(Long threadId, String userId, String lastMessage,
            Long lastMessageDate) {
        this.threadId = threadId;
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.lastMessageDate = lastMessageDate;
    }

    public Long getThreadId() {
        return this.threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(Long lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    @Override
    public String toString() {
        return "ThreadEntity{" +
                "threadId=" + threadId +
                ", userId='" + userId + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastMessageDate='" + lastMessageDate + '\'' +
                '}';
    }

    public static final class Builder {

        private Long threadId;
        private String userId;
        private String lastMessage;
        private Long lastMessageDate;

        public Builder() {
        }

        public Builder threadId(Long threadId) {
            this.threadId = threadId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder lastMessage(String lastMessage) {
            this.lastMessage = lastMessage;
            return this;
        }

        public Builder lastMessageDate(Long lastMessageDate) {
            this.lastMessageDate = lastMessageDate;
            return this;
        }

        public ThreadEntity build() {
            return new ThreadEntity(this);
        }
    }

}
