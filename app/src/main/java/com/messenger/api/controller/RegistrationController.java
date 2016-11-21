package com.messenger.api.controller;

import com.messenger.api.RetrofitManager;
import com.messenger.api.exception.NonSuccessfulResponseException;
import com.messenger.api.exception.RetrofitFailureException;
import com.messenger.api.service.RegistrationService;
import com.messenger.database.model.UserEntity;
import com.messenger.util.Base64;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author equals on 10.11.16.
 */
public class RegistrationController {

    private RegistrationService registrationService;
    private RegistrationService.Callback callback;

    public RegistrationController(RegistrationService.Callback callback) {
        this.registrationService = RetrofitManager.getInstance().getRegistrationService();
        this.callback = callback;
    }

    public void register(UserEntity userEntity) {
        Call<ResponseBody> responseCall = registrationService.createAccount(
                Base64.encodeBytes((userEntity.getLogin() + ":" + userEntity.getPassword()).getBytes()),
                userEntity);
        responseCall.enqueue(new RegistrationCallResponse());
    }

    private class RegistrationCallResponse implements Callback<ResponseBody> {

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful())
                callback.onSuccess(response);
            else
                callback.onFailure(new NonSuccessfulResponseException("Non successful response. Response code: " + response.code()));
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            callback.onFailure(new RetrofitFailureException(t.getMessage()));
        }

    }

}
