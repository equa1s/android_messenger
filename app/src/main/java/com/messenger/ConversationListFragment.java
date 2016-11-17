package com.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messenger.database.model.DaoSession;
import com.messenger.database.model.MessageEntity;
import com.messenger.database.model.ThreadEntity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;


/**
 * @author equals on 11.11.16.
 */
public class ConversationListFragment extends BaseRecyclerFragment {

    private static final String TAG = ConversationListFragment.class.getSimpleName();
    private List<ThreadEntity> threads;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, DaoSession daoSession) {
        super.onCreate(savedInstanceState);
        threads = daoSession.getThreadEntityDao().loadAll();
        Log.d(TAG, "All threads: " + Arrays.toString(threads.toArray()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.conversation_list_fragment, container, false);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public RecyclerView.Adapter setRecyclerViewAdapter() {
        // TODO: set adapter
        return new ConversationListAdapter();
    }

}
