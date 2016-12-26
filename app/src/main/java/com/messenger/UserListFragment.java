package com.messenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.messenger.animation.FadeInAnimator;
import com.messenger.database.model.ThreadEntity;
import com.messenger.database.model.ThreadEntityDao;
import com.messenger.database.model.UserEntity;
import com.messenger.database.model.UserEntityDao;
import com.messenger.ui.UserListItem;
import com.messenger.ui.divider.HorizontalDividerItemDecoration;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Fragment that displays all user's contacts.
 *
 * @author equals on 17.11.16.
 */
public class UserListFragment extends ButterKnifeFragment
        implements UserListAdapter.UserItemClickListener {

    private static final String TAG = UserListFragment.class.getSimpleName();

    public static final int _ID = 0;
    public static final int ADD_USER_REQUEST = 1;  // The request code

    private List<UserEntity> mUsers;
    private UserListAdapter mUserListAdapter;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.add_user_floating_button) FloatingActionButton addUser;
    @BindView(R.id.you_have_no_users) TextView wtFriendsLabel;

    public static UserListFragment getInstance() {
        return new UserListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mUsers = getMessengerDatabaseHelper().getUserEntityDao().loadAll();
    }

    @Override
    public int setLayout() {
        return R.layout.user_list_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new FadeInAnimator());

        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .sizeResId(R.dimen.list_divider)
                .colorResId(R.color.list_divider)
                .margin(110, 30)
                .build());

        mUserListAdapter = new UserListAdapter(getContext(), mUsers, this);
        mRecyclerView.setAdapter(mUserListAdapter);

        if (!mUsers.isEmpty()) {
            wtFriendsLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        List<UserEntity> newData = getMessengerDatabaseHelper().getUserEntityDao().loadAll();
        if (!mUserListAdapter.getData().equals(newData)) {
            mUserListAdapter.setData(newData);
        }
    }

    @Override
    public void onUserClick(UserEntity userEntity) {

        Log.d(TAG, "Passed >> " + userEntity.getLogin().toUpperCase() + " >> user. ");

        ThreadEntityDao threadEntityDao = getMessengerDatabaseHelper().getThreadEntityDao();

        QueryBuilder<ThreadEntity> threadEntityQueryBuilder = threadEntityDao.queryBuilder();
            threadEntityQueryBuilder.where(ThreadEntityDao.Properties.UserId.eq(userEntity.getLogin()));

        ThreadEntity threadEntity = threadEntityQueryBuilder.unique();

        Intent conversationActivity = new Intent(getContext(), ConversationActivity.class)
                .putExtra(ConversationActivity.RECIPIENT_LOGIN, userEntity.getLogin());

        if (threadEntity != null) {
            startActivity(conversationActivity);
        } else {
            threadEntity = new ThreadEntity.Builder()
                    .userId(userEntity.getLogin())
                    .build();

            threadEntityDao.insert(threadEntity);

            startActivity(conversationActivity);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_USER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                String login = data.getStringExtra(ConversationActivity.RECIPIENT_LOGIN); // get user_login from intent
                UserEntityDao mUserEntityDao = getMessengerDatabaseHelper().getUserEntityDao();

                // trying to get user from db
                QueryBuilder<UserEntity> oldUserEntityQueryBuilder = mUserEntityDao.queryBuilder();
                oldUserEntityQueryBuilder.where(UserEntityDao.Properties.Login.eq(login));

                // expecting unique value
                UserEntity oldUserEntity = oldUserEntityQueryBuilder.unique();

                if (oldUserEntity != null) {
                    Log.wtf(TAG, "User >> " + login.toUpperCase() + " >> exists!");
                } else {
                    UserEntity userEntity = new UserEntity.Builder()
                            .login(login)
                            .build();
                    mUserEntityDao.insert(userEntity);
                }

            }
        }
    }

    @OnClick(R.id.add_user_floating_button)
    void newUser() {
        startActivity(new Intent(getContext(), NewUserActivity.class));
    }

}
