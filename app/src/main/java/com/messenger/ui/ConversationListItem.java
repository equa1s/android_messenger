package com.messenger.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.messenger.R;
import com.messenger.database.model.ThreadEntity;
import com.messenger.util.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author equals on 21.11.16.
 */
public class ConversationListItem
        extends RelativeLayout
         implements BindConversationListItem {

    private ThreadEntity mThreadEntity;

    @BindView(R.id.user_avatar) ImageView mUserAvatar;
    @BindView(R.id.user_login) TextView mUserLogin;
    @BindView(R.id.user_last_message) TextView mUserLastMessage;

    public ConversationListItem(Context context) {
        super(context);
    }

    public ConversationListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // INFO : See https://github.com/JakeWharton/butterknife/issues/138#issuecomment-70369393
        ButterKnife.bind(this);
    }

    @Override
    public void bind(@NonNull ThreadEntity mThreadEntity) {
        this.mThreadEntity = mThreadEntity;

        // bind thread object to the view
        mUserAvatar.setImageDrawable(TextUtil.getTextDrawable(mThreadEntity.getUserId(), null));
        mUserLogin.setText(mThreadEntity.getUserId());

        String lastMessage = mThreadEntity.getLastMessage();
        if (lastMessage == null) {
            this.mUserLastMessage.setText(R.string.ConversationListItem__no_messages_with_this_user);
        } else {
            this.mUserLastMessage.setText(lastMessage);
        }
    }

    @Override
    public void unbind() {
        if (mThreadEntity != null) {
            mThreadEntity = null;
        }
    }

    public ThreadEntity getThreadEntity() {
        return mThreadEntity;
    }
}
