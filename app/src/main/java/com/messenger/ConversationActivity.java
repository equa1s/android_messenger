package com.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.messenger.database.model.DaoSession;


/**
 * @author equals on 15.11.16.
 */
public class ConversationActivity extends AbsRecyclerViewActivity {


    @Override
    protected void onPreCreate(DaoSession daoSession) {
        super.onPreCreate(daoSession);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate() {
        super.onPostCreate();
    }

    @Override
    public RecyclerView.Adapter setRecyclerViewAdapter() {
        return null;
    }

}
