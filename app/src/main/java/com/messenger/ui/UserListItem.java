package com.messenger.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.messenger.R;
import com.messenger.database.model.UserEntity;
import com.messenger.util.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * View that presents user entity
 * @author equals on 15.11.16.
 */
public class UserListItem
        extends LinearLayout
         implements BindUserListItem {

    private UserEntity userEntity;

    @BindView(R.id.user_avatar) ImageView avatar;
    @BindView(R.id.user_login) TextView login;

    public UserListItem(Context context) {
        super(context);
    }

    public UserListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // INFO : See https://github.com/JakeWharton/butterknife/issues/138#issuecomment-70369393
        ButterKnife.bind(this);
    }

    @Override
    public void bind(@NonNull UserEntity userEntity) {
        this.userEntity = userEntity;

        avatar.setImageDrawable(TextUtil.getTextDrawable(userEntity.getLogin()));
        login.setText(userEntity.getLogin());
    }

    @Override
    public void unbind() {
        if (userEntity != null)
            userEntity = null;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }
}
