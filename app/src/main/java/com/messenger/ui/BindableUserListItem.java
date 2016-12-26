package com.messenger.ui;

import android.support.annotation.NonNull;

import com.messenger.database.model.UserEntity;

/**
 * @author equals on 15.11.16.
 */
public interface BindableUserListItem {
    void bind(@NonNull UserEntity userEntity);
}
