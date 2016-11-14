package com.messenger.api.service;

import com.messenger.database.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * @author equals on 10.11.16.
 */
public interface UserService {
    @Headers("User-Agent: Messenger")
    @GET("/users/{login}")
    Call<User> getUser(@Header("Authorization") String authorization, @Path("login") String userLogin);

    interface Callback {
        void onSuccess();
        void onFailure(Throwable throwable);
    }
}
