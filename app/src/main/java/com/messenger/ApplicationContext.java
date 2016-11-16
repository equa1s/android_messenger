package com.messenger;

import android.app.Application;

import com.messenger.database.MessengerDatabaseHelper;
import com.messenger.database.model.DaoMaster;
import com.messenger.database.model.DaoSession;

import org.greenrobot.greendao.database.DatabaseOpenHelper;


/**
 * @author equals on 10.11.16.
 */
public class ApplicationContext extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseOpenHelper databaseOpenHelper = new MessengerDatabaseHelper(this, MessengerDatabaseHelper.DATABASE_NAME);
        daoSession = new DaoMaster(databaseOpenHelper.getWritableDb()).newSession();
    }

    public DaoSession daoSession() {
        return daoSession;
    }

}
