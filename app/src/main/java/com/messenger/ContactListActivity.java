package com.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;

/**
 * @author equals on 11.11.16.
 */
public class ContactListActivity extends BaseToolbarActivity {

    private static final String TAG = ConversationListActivity.class.getSimpleName();

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

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
        return R.layout.contact_list_activity;
    }

}
