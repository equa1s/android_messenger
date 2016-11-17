package com.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.messenger.database.model.DaoSession;

/**
 * @author equals on 11.11.16.
 * TODO: Change this activity (rename and add tab pager with 2 fragments: users & conversations)
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onPreCreate(DaoSession daoSession) {}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment(R.id.container, UserListFragment.getInstance(), null);

    }

    @Override
    protected int setLayout() {
        return R.layout.main_activity;
    }

    @Override
    protected void onPostCreate() {
    }

}
