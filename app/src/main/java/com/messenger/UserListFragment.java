package com.messenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.messenger.animation.FadeInAnimator;
import com.messenger.database.model.ThreadEntity;
import com.messenger.database.model.ThreadEntityDao;
import com.messenger.database.model.UserEntity;
import com.messenger.database.model.UserEntityDao;
import com.messenger.ui.UserListItem;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author equals on 17.11.16.
 */
public class UserListFragment extends ButterKnifeFragment
        implements UserListAdapter.UserItemClickListener {

    private static final String TAG = UserListFragment.class.getSimpleName();
    public static final int _ID = 0;
    public static final int ADD_USER_REQUEST = 1;  // The request code
    static final String USER_LOGIN = "user_login";

    private List<UserEntity> mUsers;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.add_user_floating_button) FloatingActionButton addUser;

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
        if (mUsers.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
        } else {
            setRecyclerView();
        }
    }

    private void setRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new FadeInAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new UserListAdapter(getContext(), mUsers, this));
    }

    @Override
    public void onUserClick(UserListItem user) {

        UserEntity userEntity = user.getUserEntity();

        ThreadEntityDao threadEntityDao = getMessengerDatabaseHelper().getThreadEntityDao();

        QueryBuilder<ThreadEntity> threadEntityQueryBuilder = threadEntityDao.queryBuilder();
            threadEntityQueryBuilder.where(ThreadEntityDao.Properties.UserId.eq(userEntity.getLogin()));

        ThreadEntity threadEntity = threadEntityQueryBuilder.unique();

        if (threadEntity != null) {
            // INFO: start {@link ConversationActivity.java} with "clicked" user
            Intent conversationActivity = new Intent(getContext(), ConversationActivity.class);
                conversationActivity.putExtra(ConversationActivity.RECIPIENT_LOGIN, userEntity.getLogin());
            startActivity(conversationActivity);
        } else {
            // TODO: create new thread and start {@link ConversationActivity.java}
            threadEntity = new ThreadEntity.Builder()
                    .userId(userEntity.getLogin())
                    .build();

            threadEntityDao.insert(threadEntity);
        }
    }


    @OnClick(R.id.add_user_floating_button)
    void newUser() {
        startActivity(new Intent(getContext(), NewUserActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_USER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                String login = data.getStringExtra(USER_LOGIN); // get user_login from intent
                UserEntityDao mUserEntityDao = getMessengerDatabaseHelper().getUserEntityDao();

                // trying to get user from db
                QueryBuilder<UserEntity> oldUserEntityQueryBuilder = mUserEntityDao.queryBuilder();
                    oldUserEntityQueryBuilder.where(UserEntityDao.Properties.Login.eq(login));

                // expecting unique value
                UserEntity oldUserEntity = oldUserEntityQueryBuilder.unique();

                if (oldUserEntity != null) {
                    // INFO : User exists > show error
                    Log.wtf(TAG, "User: " + login.toUpperCase() + " exists!");
                } else {
                    // INFO : Insert ot db
                    UserEntity userEntity = new UserEntity.Builder()
                            .login(login)
                            .build();
                        mUserEntityDao.insert(userEntity);
                }

            }
        }
    }
}
