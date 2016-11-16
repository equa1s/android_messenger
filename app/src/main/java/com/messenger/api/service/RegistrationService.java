package com.messenger.api.service;

import com.messenger.api.BaseRetrofitCallback;
import com.messenger.database.model.UserEntity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * @author equals on 10.11.16.
 */
public interface RegistrationService {

    @Headers("UserEntity-Agent: Messenger")
    @POST("/signup")
    Call<ResponseBody> createAccount(@Header("Authorization") String authorization, @Body UserEntity userEntity);

    interface Callback extends BaseRetrofitCallback {
        void onSuccess(Response response);
        void onFailure(Throwable throwable);
    }

}
