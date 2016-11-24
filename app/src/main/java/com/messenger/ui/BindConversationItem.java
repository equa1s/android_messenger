package com.messenger.ui;

import android.support.annotation.NonNull;

import com.messenger.database.model.MessageEntity;

/**
 * @author equals on 15.11.16.
 */
public interface BindConversationItem extends Unbind {
    void bind(@NonNull MessageEntity messageEntity);
}
