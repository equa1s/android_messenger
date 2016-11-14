package com.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;


/**
 * @author equals on 11.11.16.
 */
public class ConversationListActivity extends BaseToolbarActivity {

    private static final String TAG = ConversationListActivity.class.getSimpleName();


    @Override
    protected void onPreCreate() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate() {

    }

    @Override
    protected int setLayout() {
        return R.layout.conversation_list_activity;
    }

}
