package com.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.messenger.database.MessengerDatabaseHelper;
import com.messenger.database.model.UserEntity;
import com.messenger.notifications.MessageNotifier;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Activity that displays helpful information about application
 *
 * @author equals on 25.11.16.
 */
public class HelpActivity extends BaseToolbarActivity {

    private static final String TAG = HelpActivity.class.getSimpleName();

    @BindView(R.id.toolbar_title) TextView mToolbarTitle;
    @BindView(R.id.toolbar_menu_button) ImageButton mToolbarMenuButton;


    @BindView(R.id.test_login_edit_text) EditText testEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();
    }

    @Override
    protected int setLayout() {
        return R.layout.help_activity;
    }

    private void setToolbar() {
        mToolbarTitle.setText(R.string.HelpActivity__help_toolbar_title);
        mToolbarMenuButton.setVisibility(View.GONE);
    }

    @OnClick(R.id.test_add_user_button) void addTestUser() {
        UserEntity mUserEntity = new UserEntity.Builder()
                .login(testEditText.getText().toString())
                .build();

        long id = ((ApplicationContext) getApplication()).getMessengerDatabaseHelper().getUserEntityDao()
                .insert(mUserEntity);

        Log.d(TAG, "addTestUser with ID: " + id);
    }
}
