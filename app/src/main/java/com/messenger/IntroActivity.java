package com.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.messenger.preferences.MessengerSharedPreferences;

/**
 * Just a splash screen activity
 *
 * @author equals on 10.11.16.
 */
public class IntroActivity extends AppCompatActivity {

    private static final int DELAY = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);

        MessengerSharedPreferences.setFirstStart(this, true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(IntroActivity.this, RegistrationActivity.class));
                finish();
            }
        }, DELAY);
    }

}
