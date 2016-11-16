package com.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.messenger.animation.FadeInAnimator;
import com.messenger.database.model.DaoSession;
import com.messenger.database.model.UserEntity;
import com.messenger.database.model.UserEntityDao;

import org.greenrobot.greendao.query.Query;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * @author equals on 11.11.16.
 */
public class UserListActivity extends AbsRecyclerViewActivity {

    private static final String TAG = ConversationListActivity.class.getSimpleName();

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private Query<UserEntity> usersQuery;
    private List<UserEntity> userEntities;

    @Override
    protected void onPreCreate(DaoSession daoSession) {
        super.onPreCreate(daoSession);
        UserEntityDao userDao = daoSession.getUserEntityDao();
        usersQuery = userDao.queryBuilder().build();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userEntities = usersQuery.list();
        Log.d(TAG, "Users: " + Arrays.toString(userEntities.toArray()));
    }

    /*
    @Override
    protected void onPostCreate() {
        super.onPostCreate();
        setRecyclerView();
    }
    */

    /*
    @Override
    protected int setLayout() {
        return R.layout.user_list_activity;
    }
    */

    @Override
    public RecyclerView.Adapter setRecyclerViewAdapter() {
        return new UserListAdapter(this, userEntities);
    }

    /*
    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new FadeInAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new UserListAdapter(this, userEntities));
    }
    */
}
