package com.messenger.service;

import android.app.Service;

import com.messenger.ApplicationContext;
import com.messenger.database.MessengerDatabaseHelper;

/**
 * @author equals on 18.11.16.
 */

public abstract class BaseService extends Service {

    @Override
    public final void onCreate() {
        onCreate(getMessengerDatabaseHelper());
        super.onCreate();
    }

    protected void onCreate(MessengerDatabaseHelper mMessengerDatabaseHelper) {
    }

    protected MessengerDatabaseHelper getMessengerDatabaseHelper() {
        return ((ApplicationContext)getApplication()).getMessengerDatabaseHelper();
    }

}
