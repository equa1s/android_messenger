package com.messenger.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.messenger.BuildConfig;
import com.messenger.api.service.RegistrationService;
import com.messenger.api.service.UserService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author equals on 10.11.16.
 */
public class RetrofitManager {

    private static RetrofitManager instance;
    private Retrofit retrofit;

    public static RetrofitManager getInstance() {
        synchronized (RetrofitManager.class) {
            if (instance == null) {
                instance = new RetrofitManager();
            }
            return instance;
        }
    }

    private RetrofitManager() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation().create()))
                .client(new OkHttpClient())
                .baseUrl(BuildConfig.MESSENGER_URL)
                .build();
    }

    public RegistrationService getRegistrationService() {
        return retrofit.create(RegistrationService.class);
    }

    public UserService getUserService() {
        return retrofit.create(UserService.class);
    }

}
