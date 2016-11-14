package com.messenger;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.messenger.api.controller.RegistrationController;
import com.messenger.api.service.RegistrationService;
import com.messenger.database.model.User;
import com.messenger.util.Dialogs;
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

    @BindView(R.id.login_edit_text) EditText loginEditText;
    @BindView(R.id.sign_up_button) Button signUpButton;

    @Override
    protected void onPreCreate() {
        registrationController = new RegistrationController(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.RegistrationActivity_title));
        }
        signUpButton.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate() {

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
                Dialogs.showAlertDialog(this, getString(R.string.RegistrationActivity_invalid_login_title),
                        getString(R.string.RegistrationActivity_invalid_login_message));
                return;
            }

            progressDialog = ProgressDialog.show(this, getString(R.string.RegistrationActivity_progress_dialog_title),
                    getString(R.string.RegistrationActivity_progress_dialog_message));

            User user = new User();
                user.setLogin(login);
                user.setPassword(Util.getSecret(52));
            // TODO: Send request to server
            // registrationController.registerUser(user);

        }
    }

    @Override
    public void onSuccess(Response response) {
        Log.d(TAG, "onSuccess");
        progressDialog.dismiss();
        // TODO: Save user data to shared preferences
        // Save this user with id;
        // Start
    }

    @Override
    public void onFailure(Throwable throwable) {
        Log.d(TAG, "onFailure >> " + throwable.getMessage());
        progressDialog.dismiss();
        // TODO: Show warning dialog
        // Dialogs.showAlertDialog();
    }

}
