package com.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author equals on 10.11.16.
 */
public class IntroActivity extends BaseActivity {

    private static final int DELAY = 5000;

    @Override
    protected void onPreCreate() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(IntroActivity.this, RegistrationActivity.class));
                finish();
            }
        }, DELAY);
    }

    @Override
    protected void onPostCreate() {

    }

    @Override
    protected int setLayout() {
        return R.layout.intro_activity;
    }
}
