package com.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.messenger.database.model.DaoSession;

/**
 * @author equals on 16.11.16.
 * TODO: search user by using search bar
 */
public class NewUserActivity extends BaseActivity {


    @Override
    protected void onPreCreate(DaoSession daoSession) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate() {

    }

    @Override
    protected int setLayout() {
        return R.layout.new_user_activity;
    }
}
