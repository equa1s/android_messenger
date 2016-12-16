package com.messenger.preferences;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Class which represents application preferences
 *
 * @author equals on 10.11.16.
 */
public class MessengerSharedPreferences {

    private static final String PREF_USER_LOGIN = "pref_user_login";
    private static final String PREF_USER_PASSWORD = "pref_user_password";
    private static final String PREF_FIRST_START = "pref_first_start";
    private static final String PREF_COOKIES = "pref_user_cookies";

    public static boolean isFirstStart(Context context) {
        return getBooleanPreference(context, PREF_FIRST_START, false);
    }

    public static void setFirstStart(Context context, boolean value) {
        setBooleanPreference(context, PREF_FIRST_START, value);
    }

    public static boolean isUserRegistered(Context context) {
        return getUserLogin(context) != null && getUserPassword(context) != null;
    }

    public static void setUserPassword(Context context, String value) {
        setStringPreference(context, PREF_USER_PASSWORD, value);
    }

    public static String getUserPassword(Context context) {
        return getStringPreference(context, PREF_USER_PASSWORD, null);
    }

    public static void setCookies(Context context, String value) {
        setStringPreference(context, PREF_COOKIES, value);
    }

    public static String getCookies(Context context) {
        return getStringPreference(context, PREF_COOKIES, null);
    }

    public static void setUserLogin(Context context, String value) {
        setStringPreference(context, PREF_USER_LOGIN, value);
    }

    public static String getUserLogin(Context context) {
        return getStringPreference(context, PREF_USER_LOGIN, null);
    }

    private static void setBooleanPreference(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).apply();
    }

    private static boolean getBooleanPreference(Context context, String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    private static void setStringPreference(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply();
    }

    private static String getStringPreference(Context context, String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }

    private static int getIntegerPreference(Context context, String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defaultValue);
    }

    private static void setIntegerPrefrence(Context context, String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).apply();
    }

    private static boolean setIntegerPrefrenceBlocking(Context context, String key, int value) {
        return PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).commit();
    }

    private static long getLongPreference(Context context, String key, long defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, defaultValue);
    }

    private static void setLongPreference(Context context, String key, long value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(key, value).apply();
    }

}
