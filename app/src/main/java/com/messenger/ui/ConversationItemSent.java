package com.messenger.ui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.messenger.R;
import com.messenger.database.model.MessageEntity;
import com.messenger.preferences.MessengerSharedPreferences;
import com.messenger.util.DateUtils;
import com.messenger.util.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * View that presents sent message
 *
 * @author equals on 15.11.16.
 */
public class ConversationItemSent extends RelativeLayout
        implements BindableConversationItem, Unbindable {

    private MessageEntity messageEntity;

    @BindView(R.id.message_body) TextView mMessageBody;
    @BindView(R.id.message_timestamp) TextView mTimestamp;
    @BindView(R.id.user_avatar) ImageView mUserAvatar;

    public ConversationItemSent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConversationItemSent(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // INFO : See https://github.com/JakeWharton/butterknife/issues/138#issuecomment-70369393
        ButterKnife.bind(this);
    }

    @Override
    public void bind(@NonNull MessageEntity messageEntity) {
        this.messageEntity = messageEntity;

        String mFormattedReceivedTime =
                DateUtils.getBriefRelativeTimeSpanString(getContext(), messageEntity.getReceivedTime());

        this.mMessageBody.setText(messageEntity.getBody());
        this.mTimestamp.setText(mFormattedReceivedTime);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.mUserAvatar.setImageDrawable(TextUtil.getTextDrawable(MessengerSharedPreferences.getUserLogin(getContext()), getResources().getColor(R.color.conversation_activity_item_sent, null)));
        } else {
            this.mUserAvatar.setImageDrawable(TextUtil.getTextDrawable(MessengerSharedPreferences.getUserLogin(getContext()), getResources().getColor(R.color.conversation_activity_item_sent)));
        }
    }

    @Override
    public void unbind() {
        if (messageEntity != null)
            messageEntity = null;
    }

}
