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
import com.messenger.database.model.DaoSession;
import com.messenger.database.model.UserEntity;
import com.messenger.notifications.NotificationManager;
import com.messenger.preferences.MessengerSharedPreferences;
import com.messenger.util.NetworkUtil;
import com.messenger.util.Util;

import butterknife.BindView;
import retrofit2.Response;

/**
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

    @BindView(R.id.login_edit_text) EditText loginEditText;
    @BindView(R.id.sign_up_button) Button signUpButton;
    @BindView(R.id.registration_failed) LinearLayout errorLayout;
    @BindView(R.id.error_message) TextView errorMessage;
    @BindView(R.id.error_title) TextView errorTitle;

    @Override
    protected void onPreCreate(DaoSession daoSession) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registrationController = new RegistrationController(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.RegistrationActivity_title));
        }

        signUpButton.setOnClickListener(this);

    }

    @Override
    protected void onPostCreate() {
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
    }

    @Override
    protected int setLayout() {
        return R.layout.registration_activity;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_up_button) {

            String login = String.valueOf(loginEditText.getText());

            if (!Util.isLoginValid(login)) {
                handleError(getString(R.string.RegistrationActivity_invalid_login_title),
                        getString(R.string.RegistrationActivity_invalid_login_message));
                return;
            }

            if (!NetworkUtil.isNetworkAvailable(this)) {
                handleError(getString(R.string.RegistrationActivity_network_is_required),
                        getString(R.string.RegistrationActivity_network_connection_is_absent));
                return;
            }

            progressDialog = ProgressDialog.show(this, getString(R.string.RegistrationActivity_progress_dialog_title),
                    getString(R.string.RegistrationActivity_progress_dialog_message));

            userEntity = UserEntity.newBuilder()
                    .login(login)
                    .password(Util.getSecret(52))
                    .build();

            registrationController.registerUser(userEntity);
        }
    }

    @Override
    public void onSuccess(Response response) {

        Log.d(TAG, "onSuccess: " + response.message());

        progressDialog.dismiss();

        MessengerSharedPreferences.setUserLogin(this, userEntity.getLogin());
        MessengerSharedPreferences.setUserPassword(this, userEntity.getPassword());

        NotificationManager.showNotification(this,
                getString(R.string.RegistrationActivity_registration_completed),
                getString(R.string.RegistrationActivity_thank_you_for_registration));

        startActivity(new Intent(this, UserListActivity.class));
        finish();
    }

    @Override
    public void onFailure(Throwable throwable) {

        Log.d(TAG, "onFailure: " + throwable.getMessage());

        progressDialog.dismiss();

        handleError(getString(R.string.RegistrationActivity_registration_failed),
                throwable.getMessage());

    }

    private void handleError(String title, String body) {
        errorTitle.setText(title);
        errorMessage.setText(body);
        errorLayout.setVisibility(View.VISIBLE);
        errorLayout.startAnimation(fadeIn);
    }

}
