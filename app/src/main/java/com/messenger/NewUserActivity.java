package com.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.messenger.api.controller.UserController;
import com.messenger.api.service.UserService;
import com.messenger.database.MessengerDatabaseHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author equals on 16.11.16.
 */
public class NewUserActivity extends BaseToolbarActivity implements UserService.Callback {

    private static final String TAG = NewUserActivity.class.getSimpleName();

    private UserController mUserController;

    @BindView(R.id.user_search) EditText userSearch;
    @BindView(R.id.result_label) TextView resultLabel;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mUserController = new UserController(this);
    }

    @Override
    protected int setLayout() {
        return R.layout.new_user_activity;
    }

    @OnClick(R.id.add_user_button) void addUser() {
        mUserController.check();
    }

    @Override
    public void onProgress() {
        progressBar.setVisibility(View.VISIBLE);
        startProgress();
    }

    @Override
    public void onSuccess(String login) {
        handleResult(String.format(
                getString(R.string.NewUserActivity__user_s_has_been_added),
                login));
        stopProgress();
        Log.d(TAG, "Success: user " + login + " has been added. ");

    }

    @Override
    public void onFailure(Throwable throwable) {
        handleResult(String.format(
                getString(R.string.NewUserActivity__s_does_not_exist),
                userSearch.getText().toString()));

        stopProgress();
        Log.d(TAG, "onFailure: " + throwable.getMessage());
    }

    private void startProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void stopProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void handleResult(String result) {
        resultLabel.setText(result);
    }

}
