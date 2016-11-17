package com.messenger.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * @author equals on 16.11.16.
 */
@Entity(nameInDb = "threads")
public class ThreadEntity {

    @Id(autoincrement = true)
    @Property(nameInDb = "thread_id")
    private long threadId;

    @Property(nameInDb = "user_login")
    private String userId;

    private ThreadEntity(Builder builder) {
        threadId = builder.threadId;
        userId = builder.userId;
    }

    @Generated(hash = 506479455)
    public ThreadEntity(long threadId, String userId) {
        this.threadId = threadId;
        this.userId = userId;
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

    public static final class Builder {
        private long threadId;
        private String userId;

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

        public ThreadEntity build() {
            return new ThreadEntity(this);
        }
    }
}
