package com.messenger.ui;

import android.support.annotation.NonNull;

import com.messenger.database.model.ThreadEntity;

/**
 * @author equals on 21.11.16.
 */
public interface BindableConversationListItem extends Unbindable {
    void bind(@NonNull ThreadEntity mThreads);
}
