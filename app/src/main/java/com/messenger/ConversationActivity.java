package com.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.messenger.animation.FadeInAnimator;
import com.messenger.database.model.DaoSession;
import com.messenger.database.model.MessageEntity;
import com.messenger.database.model.MessageEntityDao;
import com.messenger.database.model.ThreadEntity;
import com.messenger.database.model.ThreadEntityDao;
import com.messenger.database.pojo.WebSocketIncomingMessage;
import com.messenger.database.pojo.WebSocketMessage;
import com.messenger.database.pojo.WebSocketOutgoingMessage;
import com.messenger.events.IncomingMessageEvent;
import com.messenger.notifications.MessageNotifier;
import com.messenger.notifications.NotificationManager;
import com.messenger.service.MessageRetrievalService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author equals on 15.11.16.
 */
public class ConversationActivity extends BaseActivity {

    private static final String TAG = ConversationActivity.class.getSimpleName();

    public static final String USER_LOGIN = "user_login";
    private Messenger mMessageRetrievalService;
    private ConversationAdapter mConversationAdapter;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    private String userLogin;
    private List<MessageEntity> messages;
    private DaoSession daoSession;
    private long threadId;
    boolean mBound = false;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        bindService(new Intent(this, MessageRetrievalService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPreCreate(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userLogin = bundle.getString(USER_LOGIN);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(userLogin);
        }

        loadMessages();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new FadeInAnimator());
        recyclerView.setAdapter(mConversationAdapter);
    }

    @Override
    protected int setLayout() {
        return R.layout.conversation_activity;
    }

    @Override
    protected void onPostCreate() {
        MessageNotifier.setVisibleThread(getThreadId());

        // init adapter
        mConversationAdapter = new ConversationAdapter(this, messages);

        // init recycler
        setRecyclerView();
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);

        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private void reloadAdapter() {
        loadMessages();
        // INFO~ use notifyDataSetChanged or ...
        mConversationAdapter.notifyDataSetChanged();

        // INFO~ ... create new instance of mConversationAdapter
        // mConversationAdapter = new ConversationAdapter(this, messages);
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IncomingMessageEvent messageEvent) {

        WebSocketIncomingMessage webSocketIncomingMessage = messageEvent.getMessage();

        if (MessageNotifier.isVisibleThread(threadId)) {
            // if this thread is visible redraw adapter
            reloadAdapter();
        } else {
            // send notification
            // TODO: add actions to notification
            NotificationManager.showNotification(this, webSocketIncomingMessage.getSender(),
                    webSocketIncomingMessage.getBody());
        }
    }

    private List<MessageEntity> loadMessages() {
        QueryBuilder<MessageEntity> messageEntityQueryBuilder = daoSession.getMessageEntityDao().queryBuilder();
        messageEntityQueryBuilder.where(MessageEntityDao.Properties.From.eq(userLogin));

        messages = messageEntityQueryBuilder.list();

        return messages;
    }

    private long getThreadId() {
        QueryBuilder<ThreadEntity> threadEntityQueryBuilder = daoSession.getThreadEntityDao().queryBuilder();
        threadEntityQueryBuilder.where(ThreadEntityDao.Properties.UserId.eq(userLogin));

        threadId = threadEntityQueryBuilder.unique().getThreadId();

        return threadId;
    }

    /**
     * TODO: bind {@link android.widget.Button} to this method
     * - add message to db
     * - redraw adapter
     * @param message body
     */
    @OnClick()
    private void sendMessage(String message) {
        if (mBound) {
            Date currentDate = new Date();
            long currentDateMillis = currentDate.getTime();

            WebSocketMessage webSocketMessage = new WebSocketOutgoingMessage.Builder()
                    .body(message)
                    .dateSent(currentDateMillis)
                    .dateReceived(currentDateMillis)
                    .recipient(userLogin)
                    .build();

            Message msg = Message.obtain();
                msg.obj = webSocketMessage;
                msg.what = MessageRetrievalService.SEND_MESSAGE_ACTION;
            try {
                mMessageRetrievalService.send(msg);
            } catch (RemoteException e) {
                Log.e(TAG, "Error sending a message", e);
            }
        }
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mMessageRetrievalService = new Messenger(service);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMessageRetrievalService = null;
            mBound = false;
        }
    };

}
