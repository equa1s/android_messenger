package com.messenger.api.controller;

import android.content.Context;
import android.util.Log;

import com.messenger.api.RetrofitManager;
import com.messenger.api.exception.NonSuccessfulResponseException;
import com.messenger.api.exception.RetrofitFailureException;
import com.messenger.api.service.UserService;
import com.messenger.preferences.MessengerSharedPreferences;
import com.messenger.util.Base64;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Base controller to handle user's
 *
 * @author equals on 24.11.16.
 */
public class UserController {

    private Context context;
    private UserService userService;
    private UserService.Callback callback;

    public UserController(Context context) {
        this.context = context;
        this.userService = RetrofitManager.getInstance().getUserService();
        this.callback = (UserService.Callback) context;
    }

    public void check() {

        callback.onProgress();

        Call<ResponseBody> responseBodyCall = userService.getUser(
                "Basic " + Base64.encodeBytes(
                        (MessengerSharedPreferences.getUserLogin(context) + ":" +
                                MessengerSharedPreferences.getUserPassword(context)).getBytes()));

        responseBodyCall.enqueue(new CheckCallback());
    }

    // INFO : need to refactor, because don't know what server sends me in response
    private class CheckCallback implements Callback<ResponseBody> {

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            try {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure(new NonSuccessfulResponseException("Non successful response. Response code: " + response.code() + ", body: " + response.errorBody().string()));
                }
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
