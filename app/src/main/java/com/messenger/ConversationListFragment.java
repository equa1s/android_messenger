package com.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.messenger.animation.FadeInAnimator;
import com.messenger.animation.SlideInLeftAnimator;
import com.messenger.database.model.ThreadEntity;
import com.messenger.database.model.ThreadEntityDao;
import com.messenger.database.pojo.WebSocketGetMessages;
import com.messenger.database.pojo.WebSocketIncomingMessage;
import com.messenger.events.MessageEvent;
import com.messenger.events.WebSocketMessageEvent;
import com.messenger.ui.divider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;


/**
 * Activity that presents list of all user's conversations
 *
 * @author equals on 11.11.16.
 */
public class ConversationListFragment extends ButterKnifeFragment
        implements ConversationListAdapter.ConversationClickListener{

    private static final String TAG = ConversationListFragment.class.getSimpleName();
    public static final int _ID = 1;

    private List<ThreadEntity> mThreads;
    private ConversationListAdapter mConversationListAdapter;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    public static ConversationListFragment getInstance() {
        return new ConversationListFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThreads = getThreads();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecyclerView();
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

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void setRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());

        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .sizeResId(R.dimen.list_divider)
                .colorResId(R.color.list_divider)
                .margin(110, 30)
                .build());

        mConversationListAdapter = new ConversationListAdapter(getContext(), mThreads, this);
        mRecyclerView.setAdapter(mConversationListAdapter);
    }

    private void updateRecyclerView() {
        mThreads = getThreads();
        if (!mThreads.isEmpty()) {
            mConversationListAdapter.setThreads(mThreads);
        }
    }

    /**
     * Get all user's threads.
     *
     * @return List of all threads
     */
    private List<ThreadEntity> getThreads() {
        return getMessengerDatabaseHelper()
                .getThreadEntityDao()
                .queryBuilder()
                .orderDesc(ThreadEntityDao.Properties.LastMessageDate)
                .list();
    }

    @Override
    public void onConversationClick(ThreadEntity threadEntity) {
        Intent intent = new Intent(getContext(), ConversationActivity.class);
            intent.putExtra(ConversationActivity.RECIPIENT_LOGIN, threadEntity.getUserId());
        startActivity(intent);
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {

        if (messageEvent instanceof WebSocketMessageEvent) {
            WebSocketGetMessages webSocketGetMessages = (WebSocketGetMessages) messageEvent.getMessage();
            List<WebSocketIncomingMessage> webSocketIncomingMessage = webSocketGetMessages.getMessages();
            WebSocketIncomingMessage mLastIncomingMessage = webSocketIncomingMessage.get(webSocketIncomingMessage.size() - 1);
            reload();
        }

    }

    private void reload() {
        mThreads  = getThreads();
        mConversationListAdapter.setThreads(mThreads);
    }

}
