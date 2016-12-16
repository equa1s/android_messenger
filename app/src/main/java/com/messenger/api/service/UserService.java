package com.messenger.api.service;


import com.messenger.api.BaseRetrofitCallback;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

/**
 * @author equals on 10.11.16.
 */
public interface UserService {
    @Headers("UserEntity-Agent: Messenger")
    @GET("/check")
    Call<ResponseBody> getUser(@Header("Authorization") String authorization);

    interface Callback extends BaseRetrofitCallback {
        void onSuccess(String login);
        void onFailure(Throwable throwable);
        void onProgress();
    }
}
