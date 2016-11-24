package com.messenger.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

/**
 * Entity that presents thread in database
 * @author equals on 16.11.16.
 */
@Entity(nameInDb = "threads", indexes = {
        @Index(value = "userId", unique = true)
})
public class ThreadEntity {

    @Id(autoincrement = true)
    @Property(nameInDb = "thread_id")
    private long threadId;

    @Property(nameInDb = "user_login")
    private String userId;

    @Property(nameInDb = "last_message")
    private String lastMessage;

    private ThreadEntity(Builder builder) {
        threadId = builder.threadId;
        userId = builder.userId;

    }

    @Generated(hash = 1051853799)
    public ThreadEntity(long threadId, String userId, String lastMessage) {
        this.threadId = threadId;
        this.userId = userId;
        this.lastMessage = lastMessage;
    }

    @Generated(hash = 968277741)
    public ThreadEntity() {
    }

    public long getThreadId() {
        return this.threadId;
    }

    public void setThreadId(long threadId) {
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

    public static final class Builder {
        private long threadId;
        private String userId;
        private String lastMessage;

        public Builder() {
        }

        public Builder threadId(long threadId) {
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

        public ThreadEntity build() {
            return new ThreadEntity(this);
        }
    }
}
