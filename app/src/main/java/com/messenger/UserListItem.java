package com.messenger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.messenger.database.model.UserEntity;
import com.messenger.util.ColorUtil;

/**
 * @author equals on 15.11.16.
 */
public class UserListItem
        extends LinearLayout
         implements BindUserListItem {

    private UserEntity userEntity;
    private ImageView avatar;
    private TextView login;

    public UserListItem(Context context) {
        super(context);
    }

    public UserListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.avatar = (ImageView) findViewById(R.id.user_avatar);
        this.login = (TextView) findViewById(R.id.user_login);
    }

    @Override
    public void bind(@NonNull UserEntity userEntity) {
        this.userEntity = userEntity;
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(userEntity.getLogin().charAt(0)), ColorUtil.getRandomColor());
        avatar.setImageDrawable(drawable);
        login.setText(userEntity.getLogin());
    }

    @Override
    public void unbind() {
        if (userEntity != null)
            userEntity = null;
    }

}
