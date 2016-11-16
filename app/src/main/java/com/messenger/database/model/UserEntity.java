package com.messenger.database.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * @author equals on 10.11.16.
 */
@Entity(nameInDb = "users", indexes = {
        @Index(value = "login", unique = true),
        @Index(value = "id", unique = true)
})
public class UserEntity {

    @Id(autoincrement = true)
    private long id;
    @Property(nameInDb = "login")
    private String login;
    @Transient
    private String password;

    private UserEntity() {
    }

    private UserEntity(Builder builder) {
        setId(builder.id);
        setLogin(builder.login);
        setPassword(builder.password);
    }

    @Generated(hash = 926557230)
    public UserEntity(long id, String login) {
        this.id = id;
        this.login = login;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "login='" + login + '\'' +
                '}';
    }

    /**
     * {@code UserEntity} builder static inner class.
     */
    public static final class Builder {

        private long id;
        private String login;
        private String password;

        private Builder() {
        }

        /**
         * Sets the {@code login} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param login the {@code login} to set
         * @return a reference to this Builder
         */
        public Builder login(String login) {
            this.login = login;
            return this;
        }

        /**
         * Sets the {@code password} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param password the {@code password} to set
         * @return a reference to this Builder
         */
        public Builder password(String password) {
            this.password = password;
            return this;
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
         * Returns a {@code UserEntity} built from the parameters previously set.
         *
         * @return a {@code UserEntity} built with parameters of this {@code UserEntity.Builder}
         */
        public UserEntity build() {
            return new UserEntity(this);
        }
    }
}
