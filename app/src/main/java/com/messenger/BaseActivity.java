package com.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * @author equals on 11.11.16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void onPreCreate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onPreCreate();
        super.onCreate(savedInstanceState);
        onPostCreate();
        setContentView(setLayout());
        ButterKnife.bind(this);
    }

    protected abstract void onPostCreate();

    protected abstract int setLayout();
}
