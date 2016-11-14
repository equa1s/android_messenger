package com.messenger.api;

import com.messenger.BuildConfig;
import com.messenger.api.service.RegistrationService;
import com.messenger.api.service.UserService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author equals on 10.11.16.
 */
public class RetrofitManager {

    private static final Object lock = new Object();
    private static RetrofitManager instance;
    private static Retrofit retrofit;

    public static RetrofitManager getInstance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new RetrofitManager();
            }
            return instance;
        }
    }

    private RetrofitManager() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .client(new OkHttpClient())
                .baseUrl(BuildConfig.MESSENGER_URL)
                .build();
    }

    private static Retrofit getRetrofit() {
        return retrofit;
    }

    public RegistrationService getRegistrationService() {
        return retrofit.create(RegistrationService.class);
    }

    public UserService getUserService() {
        return retrofit.create(UserService.class);
    }

}
