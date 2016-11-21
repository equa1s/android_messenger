package com.messenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messenger.database.model.DaoSession;
import com.messenger.database.model.ThreadEntity;
import com.messenger.database.model.ThreadEntityDao;
import com.messenger.database.model.UserEntity;
import com.messenger.database.model.UserEntityDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author equals on 17.11.16.
 */
public class UserListFragment extends BaseRecyclerFragment implements UserListAdapter.UserItemClickListener {

    private static final String TAG = UserListFragment.class.getSimpleName();
    public static final int ADD_USER_REQUEST = 1;  // The request code
    static final String USER_LOGIN = "user_login";

    private List<UserEntity> mUsers;
    private DaoSession mDaoSession;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.add_user_floating_button) FloatingActionButton addUser;

    public static UserListFragment getInstance() {
        return new UserListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, DaoSession daoSession) {
        super.onCreate(savedInstanceState);
        this.mDaoSession = daoSession;
        mUsers = daoSession.getUserEntityDao().loadAll();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_list_fragment, container, false);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public RecyclerView.Adapter setRecyclerViewAdapter() {
        return new UserListAdapter(getContext(), mUsers, this);
    }

    @Override
    public void onUserClick(UserListItem user) {

        UserEntity userEntity = user.getUserEntity();

        ThreadEntityDao threadEntityDao = mDaoSession.getThreadEntityDao();

        QueryBuilder<ThreadEntity> threadEntityQueryBuilder = threadEntityDao.queryBuilder();
            threadEntityQueryBuilder.where(ThreadEntityDao.Properties.UserId.eq(userEntity.getLogin()));

        ThreadEntity threadEntity = threadEntityQueryBuilder.unique();

        if (threadEntity != null) {
            // INFO: start {@link ConversationActivity.java} with clicked user
            Intent conversationActivity = new Intent(getContext(), ConversationActivity.class);
                conversationActivity.putExtra(ConversationActivity.USER_LOGIN, userEntity.getLogin());
            startActivity(conversationActivity);
        } else {
            // TODO: create new thread and start {@link ConversationActivity.java}
            threadEntity = new ThreadEntity.Builder()
                    .userId(userEntity.getLogin())
                    .build();

            threadEntityDao.insert(threadEntity);
        }
    }

    @OnClick(R.id.add_user_floating_button) void addUser() {
        startActivityForResult(new Intent(getContext(), NewUserActivity.class), ADD_USER_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_USER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                String login = data.getStringExtra(USER_LOGIN); // get user_login from intent
                UserEntityDao mUserEntityDao = mDaoSession.getUserEntityDao();

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
