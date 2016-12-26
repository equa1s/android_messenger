package com.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.messenger.database.MessengerDatabaseHelper;

import butterknife.ButterKnife;

/**
 * Base ButterKnife and database handler activity.
 *
 * @author equals on 11.11.16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        ButterKnife.bind(this);
    }

    protected abstract int setLayout();

    public MessengerDatabaseHelper getMessengerDatabaseHelper() {
        return ((ApplicationContext)getApplication()).getMessengerDatabaseHelper();
    }

}
