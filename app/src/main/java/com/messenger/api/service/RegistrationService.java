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
 * General {@link retrofit2.Retrofit} interface for work with in-app registration.
 *
 * @author equals on 10.11.16.
 */
public interface RegistrationService {

    @Headers("UserEntity-Agent: Messenger")
    @POST("/reg")
    Call<ResponseBody> signUp(@Header("Authorization") String auth, @Body UserEntity userEntity);

    @Headers("UserEntity-Agent: Messenger")
    @POST("/login")
    Call<ResponseBody> signIn(@Header("Authorization") String auth, @Body UserEntity userEntity);

    interface Callback extends BaseRetrofitCallback {
        void onSuccess(Response response);
        void onFailure(Throwable throwable);
    }

}
