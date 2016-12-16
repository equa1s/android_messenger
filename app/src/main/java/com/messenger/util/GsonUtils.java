package com.messenger.util;

import com.google.gson.Gson;

/**
 *  Simple wrapper for GSON library.
 *
 * @author equals on 15.12.16.
 */
public class GsonUtils {

    private static final Gson instance = new Gson();

    public static <T> T fromJson(String input, Class<T> clazz) {
        return instance.fromJson(input, clazz);
    }

    public static String toJson(Object object) {
        return instance.toJson(object);
    }

}
