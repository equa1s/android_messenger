package com.messenger.database;

import android.content.Context;

import com.messenger.database.model.DaoMaster;

/**
 * @author equals on 10.11.16.
 */
public class MessengerDatabaseHelper extends DaoMaster.OpenHelper {

    public static final String DATABASE_NAME = "messenger-db";

    public MessengerDatabaseHelper(Context context, String name) {
        super(context, name);
    }

}
