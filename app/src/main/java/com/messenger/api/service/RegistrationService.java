package com.messenger.api.service;

import com.messenger.api.BaseRetrofitCallback;
import com.messenger.database.model.User;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * @author equals on 10.11.16.
 */
public interface RegistrationService {

    @Headers("User-Agent: Messenger")
    @POST("/registration")
    Call<Response> createAccount(@Body User user);

    interface Callback extends BaseRetrofitCallback {
        void onSuccess(Response response);
        void onFailure(Throwable throwable);
    }

}
