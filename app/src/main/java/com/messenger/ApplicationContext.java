package com.messenger;

import android.app.Application;

import com.messenger.database.MessengerDatabaseHelper;


/**
 * @author equals on 10.11.16.
 */
public class ApplicationContext extends Application {

    private MessengerDatabaseHelper mMessengerDatabaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mMessengerDatabaseHelper = MessengerDatabaseHelper.getInstance(getApplicationContext());
    }

    public MessengerDatabaseHelper getMessengerDatabaseHelper() {
        return mMessengerDatabaseHelper;
    }
}
