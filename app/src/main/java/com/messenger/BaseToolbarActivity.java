package com.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.messenger.preferences.MessengerSharedPreferences;
import com.messenger.service.MessageService;

import butterknife.ButterKnife;

/**
 * Base toolbar activity which helps to handleMessage current application state.
 *
 * @author equals on 10.11.16.
 */
public abstract class BaseToolbarActivity extends BaseActivity {

    private static final String TAG = BaseToolbarActivity.class.getSimpleName();

    private static final int STATE_FIRST_START = 0;
    private static final int STATE_NEED_REGISTRATION = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        routeApplicationState();
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, MessageService.class));
    }

    private void routeApplicationState() {
        int state = getApplicationState();
        Log.d(TAG, "Current app state: " + state);
        Intent intent = getIntentForState(state);
        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }

    private Intent getIntentForState(int state) {
        switch (state) {
            case STATE_NEED_REGISTRATION:
                return new Intent(this, RegistrationActivity.class);
            case STATE_FIRST_START:
                return new Intent(this, IntroActivity.class);
            default:
                return null;
        }
    }

    private int getApplicationState() {
         if (MessengerSharedPreferences.isFirstStart(this)) {
             return STATE_FIRST_START;
        } else if (!MessengerSharedPreferences.isUserRegistered(this)) {
             return STATE_NEED_REGISTRATION;
         }
        return -1;
    }
}
