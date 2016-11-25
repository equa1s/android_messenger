package com.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.messenger.database.MessengerDatabaseHelper;
import com.messenger.preferences.MessengerSharedPreferences;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Activity that displays two tab fragments: conversations and friends.
 *
 * @author equals on 11.11.16.
 */
public class MainActivity extends BaseToolbarActivity implements PopupMenu.OnMenuItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tabs) TabLayout mTabs;
    @BindView(R.id.view_pager) ViewPager mPager;

    @Override
    protected void onPreCreate(MessengerDatabaseHelper mMessengerDatabaseHelper) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Current user: " + MessengerSharedPreferences.getUserLogin(this));

        if (getSupportActionBar() != null) {
            setSupportActionBar(mToolbar);
        }

        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mTabs.setupWithViewPager(mPager);
        mPager.setCurrentItem(1);
    }

    @Override
    protected int setLayout() {
        return R.layout.main_activity;
    }

    @Override
    protected void onPostCreate() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_prefs:
                // TODO : release prefs activity
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;

            case R.id.menu_help:
                // TODO : release help activity
                startActivity(new Intent(this, HelpActivity.class));
                return true;

        }
        return false;
    }

    @OnClick(R.id.menu_button) void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.main_menu);
            popup.show();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private final String[] TITLES = {"Friends", "Conversations"};

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case UserListFragment._ID:
                    fragment = UserListFragment.getInstance();
                    break;
                case ConversationListFragment._ID:
                    fragment = ConversationListFragment.getInstance();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }


}
