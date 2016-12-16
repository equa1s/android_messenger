package com.messenger.database.model;


import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Entity that presents users table in database.
 *
 * @author equals on 10.11.16.
 */
@Entity(nameInDb = "users", indexes = {
        @Index(value = "login", unique = true)
})
public class UserEntity {

    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "login")
    @Expose
    private String login;
    @Transient
    private String password;

    public UserEntity() {
    }

    private UserEntity(Builder builder) {
        setId(builder.id);
        setLogin(builder.login);
        setPassword(builder.password);
    }

    @Generated(hash = 1794783451)
    public UserEntity(Long id, String login) {
        this.id = id;
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static final class Builder {

        private Long id;
        private String login;
        private String password;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(this);
        }
    }
    
}
