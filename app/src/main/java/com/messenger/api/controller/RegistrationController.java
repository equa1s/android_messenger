package com.messenger.api.controller;

import com.messenger.api.RetrofitManager;
import com.messenger.api.exception.NonSuccessfulResponseException;
import com.messenger.api.service.RegistrationService;
import com.messenger.database.model.User;

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

    public void registerUser(User user) {
        Call<Response> responseCall = registrationService.createAccount(user);
        responseCall.enqueue(new RegistrationCallResponse());
    }

    private class RegistrationCallResponse implements Callback<Response> {

        @Override
        public void onResponse(Call<Response> call, Response<Response> response) {
            if (response.isSuccessful())
                callback.onSuccess(response);
            else
                callback.onFailure(new NonSuccessfulResponseException("Non successful response. Response code: " + response.code()));
        }

        @Override
        public void onFailure(Call<Response> call, Throwable t) {
            callback.onFailure(new NonSuccessfulResponseException(t.getMessage()));
        }

    }

}
