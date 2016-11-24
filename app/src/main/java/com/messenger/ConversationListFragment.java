package com.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messenger.animation.FadeInAnimator;
import com.messenger.database.model.ThreadEntity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;


/**
 * Activity that presents list of all user's conversations
 * @author equals on 11.11.16.
 */
public class ConversationListFragment extends ButterKnifeFragment
        implements ConversationListAdapter.ConversationClickListener{

    private static final String TAG = ConversationListFragment.class.getSimpleName();
    public static final int _ID = 1;

    private List<ThreadEntity> mThreads;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    public static ConversationListFragment getInstance() {
        return new ConversationListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThreads = ((ApplicationContext)getActivity().getApplication()).getMessengerDatabaseHelper().getThreadEntityDao().loadAll();
        Log.d(TAG, "All mThreads: " + Arrays.toString(mThreads.toArray()));
    }

    @Override
    public int setLayout() {
        return R.layout.conversation_list_fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // INFO : if user has no conversation's
        if (!mThreads.isEmpty()) {
            setRecyclerView();
        } else {
            mRecyclerView.setVisibility(View.GONE);
            Log.d(TAG, "You have no conversation's");
        }

    }

    private void setRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new FadeInAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new ConversationListAdapter(getContext(), mThreads, this));
    }

    @Override
    public void onConversationClick(ThreadEntity threadEntity) {
        Intent intent = new Intent(getContext(), ConversationActivity.class);
            intent.putExtra(ConversationActivity.RECIPIENT_LOGIN, threadEntity.getUserId());
        startActivity(intent);
    }

}
