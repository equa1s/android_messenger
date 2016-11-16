package com.messenger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.messenger.database.model.MessageEntity;

/**
 * @author equals on 15.11.16.
 */
public class ConversationItemReceived extends RelativeLayout implements BindConversationItem {

    private MessageEntity messageEntity;
    // TODO: create messageEntity items, such as: body, received date and from

    public ConversationItemReceived(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConversationItemReceived(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // TODO: inflate layout
    }

    @Override
    public void bind(@NonNull MessageEntity messageEntity) {
        this.messageEntity = messageEntity;
        // TODO: bind messageEntity object to view
    }

    @Override
    public void unbind() {
        if (messageEntity != null)
            messageEntity = null;
    }

}
