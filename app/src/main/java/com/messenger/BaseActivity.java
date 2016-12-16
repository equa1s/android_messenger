package com.messenger;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.messenger.database.MessengerDatabaseHelper;

import butterknife.ButterKnife;

/**
 * @author equals on 11.11.16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void onPreCreate(MessengerDatabaseHelper mMessengerDatabaseHelper);

    @Override
    protected void onStart() {
        super.onStart();
        onPreCreate(getMessengerDatabaseHelper());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        ButterKnife.bind(this);
        onPostCreate();
    }

    protected abstract void onPostCreate();

    protected abstract int setLayout();

    public MessengerDatabaseHelper getMessengerDatabaseHelper() {
        return ((ApplicationContext)getApplication()).getMessengerDatabaseHelper();
    }

}
