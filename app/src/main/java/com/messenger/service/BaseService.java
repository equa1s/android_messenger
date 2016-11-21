package com.messenger.service;

import android.app.Service;

import com.messenger.ApplicationContext;
import com.messenger.database.model.DaoSession;

/**
 * @author equals on 18.11.16.
 */

public abstract class BaseService extends Service {

    @Override
    public final void onCreate() {
        onCreate(daoSession());
        super.onCreate();
    }

    protected void onCreate(DaoSession daoSession) {
    }

    protected DaoSession daoSession() {
        return ((ApplicationContext)getApplication()).daoSession();
    }

}
