package com.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.messenger.database.model.DaoSession;
import com.messenger.preferences.MessengerSharedPreferences;

/**
 * @author equals on 10.11.16.
 */
public abstract class BaseToolbarActivity extends BaseActivity {

    private static final String TAG = BaseToolbarActivity.class.getSimpleName();

    private static final int STATE_NEED_REGISTRATION = 1;

    @Override
    protected void onPreCreate(DaoSession daoSession) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        routeApplicationState();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate() {
    }

    private void routeApplicationState() {
        int state = getApplicationState();
        Intent intent = getIntentForState(state);
        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }

    private Intent getIntentForState(int state) {
        switch (state) {
            case STATE_NEED_REGISTRATION:
                return new Intent(this, IntroActivity.class);
            default:
                return null;
        }
    }

    private int getApplicationState() {
        if (!MessengerSharedPreferences.isUserRegistered(this)) {
            return STATE_NEED_REGISTRATION;
        }
        return -1;
    }
}
