package com.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.messenger.database.model.DaoSession;

import butterknife.ButterKnife;

/**
 * @author equals on 11.11.16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void onPreCreate(DaoSession daoSession);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onPreCreate(daoSession());
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        ButterKnife.bind(this);
        onPostCreate();
    }

    protected abstract void onPostCreate();

    protected abstract int setLayout();

    private DaoSession daoSession() {
        return ((ApplicationContext)getApplication()).daoSession();
    }
}
