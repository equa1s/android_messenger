package com.messenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.messenger.api.controller.RegistrationController;
import com.messenger.api.service.RegistrationService;
import com.messenger.database.model.UserEntity;
import com.messenger.notifications.NotificationManager;
import com.messenger.preferences.MessengerSharedPreferences;
import com.messenger.util.NetworkUtil;
import com.messenger.util.Util;

import butterknife.BindView;
import retrofit2.Response;

/**
 * Activity which presents in-app registration/login.
 *
 * @author equals on 10.11.16.
 */
public class RegistrationActivity
        extends BaseActivity
            implements RegistrationService.Callback,
                        View.OnClickListener {

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    private RegistrationController registrationController;
    private ProgressDialog progressDialog;
    private UserEntity userEntity;

    private Animation fadeIn;

    @BindView(R.id.login_edit_text) EditText mLogin;
    @BindView(R.id.password_edit_text) EditText mPassword;
    @BindView(R.id.sign_up_button) Button mSignUp;
    @BindView(R.id.sign_in_button) Button mSignIn;
    @BindView(R.id.registration_failed) LinearLayout errorLayout;
    @BindView(R.id.error_message) TextView errorMessage;
    @BindView(R.id.error_title) TextView errorTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registrationController = new RegistrationController(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.RegistrationActivity__title));
        }

        mSignUp.setOnClickListener(this);
        mSignIn.setOnClickListener(this);

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

    }

    @Override
    protected int setLayout() {
        return R.layout.registration_activity;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_up_button) {
            handleSignUp();
        }  else if (view.getId() == R.id.sign_in_button) {
            handleSignIn();
        }
    }

    private void handleSignUp() {
        String mLogin = String.valueOf(this.mLogin.getText());
        String mPassword = String.valueOf(this.mPassword.getText());

        if (!Util.isLoginValid(mLogin)) {
            handleError(getString(R.string.RegistrationActivity__invalid_login_title),
                    getString(R.string.RegistrationActivity__invalid_login_message));
            return;
        }

        if (!Util.isPasswordValid(mPassword)) {
            handleError(getString(R.string.RegistrationActivity__password_is_invalid),
                    getString(R.string.RegistrationActivity__password_is_invalid_message));
            return;
        }

        if (!NetworkUtil.isNetworkAvailable(this)) {
            handleError(getString(R.string.RegistrationActivity__network_is_required),
                    getString(R.string.RegistrationActivity__network_connection_is_absent));
            return;
        }

        progressDialog = ProgressDialog.show(this, getString(R.string.RegistrationActivity__progress_dialog_sign_up_title),
                getString(R.string.RegistrationActivity__progress_dialog_sign_up_message));

        userEntity = new UserEntity.Builder()
                .login(mLogin)
                .password(mPassword)
                .build();

        registrationController.signUp(userEntity);
    }

    private void handleSignIn() {
        String mLogin = String.valueOf(this.mLogin.getText());
        String mPassword = String.valueOf(this.mPassword.getText());

        if (!Util.isLoginValid(mLogin)) {
            handleError(getString(R.string.RegistrationActivity__invalid_login_title),
                    getString(R.string.RegistrationActivity__invalid_login_message));
            return;
        }

        if (!Util.isPasswordValid(mPassword)) {
            handleError(getString(R.string.RegistrationActivity__password_is_invalid),
                    getString(R.string.RegistrationActivity__password_is_invalid_message));
            return;
        }

        if (!NetworkUtil.isNetworkAvailable(this)) {
            handleError(getString(R.string.RegistrationActivity__network_is_required),
                    getString(R.string.RegistrationActivity__network_connection_is_absent));
            return;
        }

        progressDialog = ProgressDialog.show(this, getString(R.string.RegistrationActivity__progress_dialog_sign_in_title),
                getString(R.string.RegistrationActivity__progress_dialog_sign_in_message));

        userEntity = new UserEntity.Builder()
                .login(mLogin)
                .password(mPassword)
                .build();

        registrationController.signIn(userEntity);
    }

    @Override
    public void onSuccess(Response response) {

        Log.d(TAG, "onSuccess: " + response.message());

        progressDialog.dismiss();

        // if user is not registered then add him to prefs and start main activity
        if (!MessengerSharedPreferences.isUserRegistered(this)) {
            MessengerSharedPreferences.setUserLogin(this, userEntity.getLogin());
            MessengerSharedPreferences.setUserPassword(this, userEntity.getPassword());

            NotificationManager.showNotification(this,
                    getString(R.string.RegistrationActivity__registration_completed),
                    getString(R.string.RegistrationActivity__thank_you_for_registration));
        }

        // user exists, do not need to re-register him, just route him to main activity
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onFailure(Throwable throwable) {

        Log.d(TAG, "onFailure: " + throwable.getMessage());

        progressDialog.dismiss();

        handleError(getString(R.string.RegistrationActivity__sign_in_sign_up_failed),
                throwable.getMessage().substring(0, 100).concat("..."));

    }

    private void handleError(String title, String body) {
        errorTitle.setText(title);
        errorMessage.setText(body);
        errorLayout.setVisibility(View.VISIBLE);
        errorLayout.startAnimation(fadeIn);
    }

}
