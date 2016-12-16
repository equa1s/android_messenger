package com.messenger.api.controller;

import android.content.Context;

import com.messenger.api.RetrofitManager;
import com.messenger.api.exception.NonSuccessfulResponseException;
import com.messenger.api.exception.RetrofitFailureException;
import com.messenger.api.service.RegistrationService;
import com.messenger.database.model.UserEntity;
import com.messenger.util.Base64;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class controls in-app sign_up/sign_in
 * though Retrofit (type-safe HTTP client for Android and Java)
 * where we use Jackson converter factory and OkHttp http client
 *
 * @author equals on 10.11.16.
 */
public class RegistrationController {

    private RegistrationService registrationService;
    private RegistrationService.Callback callback;

    public RegistrationController(Context context) {
        this.registrationService = RetrofitManager.getInstance().getRegistrationService();
        this.callback = (RegistrationService.Callback)context;
    }

    /**
     * Send a request to create an account
     * @param userEntity user which will be created
     */
    public void signUp(UserEntity userEntity) {
        Call<ResponseBody> responseCall = registrationService.signUp(
                "Basic " + Base64.encodeBytes((userEntity.getLogin() + ":" + userEntity.getPassword()).getBytes()),
                userEntity);
        responseCall.enqueue(new SignUpCallResponse());
    }

    /**
     * Send a request to check user on existence
     * @param userEntity user which will be signed up
     */
    public void signIn(UserEntity userEntity) {
        Call<ResponseBody> responseBodyCall = registrationService.signIn(
                "Basic " + Base64.encodeBytes((userEntity.getLogin() + ":" + userEntity.getPassword()).getBytes()),
                userEntity);
        responseBodyCall.enqueue(new SignInCallResponse());
    }

    private class SignInCallResponse implements Callback<ResponseBody> {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                callback.onSuccess(response);
            } else {
                try {
                    callback.onFailure(new NonSuccessfulResponseException("Non successful response. Response code: " + response.code() + ", body: " + response.errorBody().string()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            callback.onFailure(new RetrofitFailureException("Retrofit failure: " + t.getMessage()));
        }
    }

    private class SignUpCallResponse implements Callback<ResponseBody> {

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful())
                callback.onSuccess(response);
            else
                try {
                    callback.onFailure(new NonSuccessfulResponseException("Non successful response. Response code: " + response.code() + ", body: " + response.errorBody().string()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            callback.onFailure(new RetrofitFailureException("Retrofit failure: " + t.getMessage()));
        }

    }

}
