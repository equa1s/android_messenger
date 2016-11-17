package com.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messenger.database.model.DaoSession;
import com.messenger.database.model.ThreadEntity;
import com.messenger.database.model.UserEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author equals on 17.11.16.
 */
public class UserListFragment extends BaseRecyclerFragment implements UserListAdapter.UserItemClickListener {

    private List<UserEntity> users;
    private DaoSession daoSession;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.add_user_floating_button) FloatingActionButton addUser;

    public static UserListFragment getInstance() {
        return new UserListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, DaoSession daoSession) {
        super.onCreate(savedInstanceState);
        users = daoSession.getUserEntityDao().loadAll();
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
        return new UserListAdapter(getContext(), users, this);
    }

    @Override
    public void onUserClick(UserListItem user) {
        UserEntity userEntity = user.getUserEntity();
        ThreadEntity threadEntity = (ThreadEntity) daoSession.getThreadEntityDao().queryRaw("userId = ?", userEntity.getLogin());
        if (threadEntity != null) {
            // TODO: start {@link ConversationActivity.java}
        } else {
            // TODO: create new thread and start {@link ConversationActivity.java}
        }
    }

    @OnClick(R.id.add_user_floating_button) void addUser() {
        startActivity(new Intent(getContext(), NewUserActivity.class));
    }
}
