package com.messenger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messenger.database.model.UserEntity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author equals on 14.11.16.
 */
class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private List<UserEntity> data = null;
    private LayoutInflater inflater;
    private UserItemClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(final @NonNull View itemView, final @NonNull UserItemClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onUserClick((UserListItem) getItem());
                }
            });
        }

        BindUserListItem getItem() {
            return (BindUserListItem) itemView;
        }
    }

    UserListAdapter(Context context, List<UserEntity> data, UserItemClickListener listener) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;

        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserListItem userListItem =  (UserListItem) inflater.inflate(R.layout.user_list_item, parent, false);
        return new ViewHolder(userListItem, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserEntity userEntity = data.get(position);
            holder.getItem().unbind();
            holder.getItem().bind(userEntity);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void insertUser(UserEntity userEntity) {
        data.add(userEntity);
        sortUsers();
        notifyItemInserted(data.indexOf(userEntity));
    }

    public void removeUser(UserEntity userEntity) {
        data.remove(userEntity);
        sortUsers();
        notifyItemRemoved(data.indexOf(userEntity));
    }

    private void sortUsers() {
        Collections.sort(data, new Comparator<UserEntity>() {
            @Override
            public int compare(UserEntity userEntity0, UserEntity userEntity1) {
                return userEntity0.getLogin().compareTo(userEntity1.getLogin());
            }
        });
    }

    interface UserItemClickListener {
        void onUserClick(UserListItem user);
    }

}
