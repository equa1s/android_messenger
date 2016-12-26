package com.messenger.database.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Basic message uses by web socket.
 *
 * @author equals on 13.12.16.
 */
public class WebSocketMessage {

    @Expose @SerializedName("method") private String method;
    @Expose @SerializedName("data") private IWebSocketData data;
    @Expose @SerializedName("count") private Integer count;

    private WebSocketMessage(Builder builder) {
        method = builder.method;
        data = builder.data;
        count = builder.count;
    }

    public IWebSocketData getWebSocketData() {
        return data;
    }

    public String getMethod() {
        return method;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "\nWebSocketMessage { " +
                "\n\tmethod='" + method + '\'' +
                ", \n\t\tdata=" + data +
                ", \n\t\tcount=" + count +
                "\n}\n";
    }

    public static final class Builder {

        @Expose @SerializedName("method") private String method;
        @Expose @SerializedName("data") private IWebSocketData data;
        @Expose @SerializedName("count") private Integer count;

        public Builder() {
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder data(IWebSocketData data) {
            this.data = data;
            return this;
        }

        public Builder count(Integer count) {
            this.count = count;
            return this;
        }

        public WebSocketMessage build() {
            return new WebSocketMessage(this);
        }
    }
}
