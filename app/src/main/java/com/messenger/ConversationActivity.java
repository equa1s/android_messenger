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
import android.view.View;
import android.widget.EditText;

import com.messenger.animation.FadeInAnimator;
import com.messenger.database.MessengerDatabaseHelper;
import com.messenger.database.model.MessageEntity;
import com.messenger.database.model.MessageEntityDao;
import com.messenger.database.model.ThreadEntity;
import com.messenger.database.model.ThreadEntityDao;
import com.messenger.database.pojo.WebSocketIncomingMessage;
import com.messenger.database.pojo.WebSocketMessage;
import com.messenger.database.pojo.WebSocketOutgoingMessage;
import com.messenger.events.IncomingMessageEvent;
import com.messenger.events.MessageEvent;
import com.messenger.events.OutgoingMessageEvent;
import com.messenger.notifications.MessageNotifier;
import com.messenger.notifications.NotificationManager;
import com.messenger.service.MessageService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

import butterknife.BindView;


/**
 * Activity for displaying message thread with specified user
 * also send and receive messages.
 *
 * @author equals on 15.11.16.
 */
public class ConversationActivity extends BaseToolbarActivity implements View.OnClickListener {

    private static final String TAG = ConversationActivity.class.getSimpleName();

    public static final String RECIPIENT_LOGIN = "recipient_login";

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.message_input_panel) EditText mInputPanel;

    private Messenger mMessageRetrievalService;
    private ConversationAdapter mConversationAdapter;
    private String mRecipient;
    private List<MessageEntity> mMessages;
    private MessengerDatabaseHelper mMessengerDatabaseHelper;
    private long mThreadId;
    boolean mBound = false;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        bindService(new Intent(this, MessageService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPreCreate(MessengerDatabaseHelper mMessengerDatabaseHelper) {
        this.mMessengerDatabaseHelper = mMessengerDatabaseHelper;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRecipient = bundle.getString(RECIPIENT_LOGIN);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mRecipient);
        }

        loadMessages();
    }

    private void setRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new FadeInAnimator());
        mRecyclerView.setAdapter(mConversationAdapter);
    }

    @Override
    protected int setLayout() {
        return R.layout.conversation_activity;
    }

    @Override
    protected void onPostCreate() {
        MessageNotifier.setVisibleThread(getThreadId());

        // init adapter
        mConversationAdapter = new ConversationAdapter(this, mMessages);

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

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {

        if (messageEvent instanceof OutgoingMessageEvent) {
            // get outgoing message
            WebSocketOutgoingMessage webSocketOutgoingMessage = (WebSocketOutgoingMessage) messageEvent.getMessage();
            // just reload view because we see current conversation
            reload();
        } else if (messageEvent instanceof IncomingMessageEvent) {
            // get incoming message
            WebSocketIncomingMessage webSocketIncomingMessage = (WebSocketIncomingMessage) messageEvent.getMessage();
            // if conversation is not visible
            if (!MessageNotifier.isVisibleThread(mThreadId)) {
                // then notify user about new message
                NotificationManager.showNotification(this, webSocketIncomingMessage.getSender(),
                        webSocketIncomingMessage.getBody());
            } else {
                // otherwise just reload an adapter
                reload();
            }
        }
    }

    /**
     * Load messages from database
     * and redraw conversation adapter
     */
    private void reload() {
        loadMessages();
        // INFO~ use notifyDataSetChanged or ...
        mConversationAdapter.notifyDataSetChanged();

        // INFO~ ... create new instance of mConversationAdapter
        // mConversationAdapter = new ConversationAdapter(this, mMessages);
    }


    private List<MessageEntity> loadMessages() {
        QueryBuilder<MessageEntity> messageEntityQueryBuilder = mMessengerDatabaseHelper.getMessageEntityDao().queryBuilder();
        messageEntityQueryBuilder.where(MessageEntityDao.Properties.From.eq(mRecipient));

        mMessages = messageEntityQueryBuilder.list();

        return mMessages;
    }

    private long getThreadId() {
        QueryBuilder<ThreadEntity> threadEntityQueryBuilder = mMessengerDatabaseHelper.getThreadEntityDao().queryBuilder();
        threadEntityQueryBuilder.where(ThreadEntityDao.Properties.UserId.eq(mRecipient));

        mThreadId = threadEntityQueryBuilder.unique().getThreadId();

        return mThreadId;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_send_button:

                String mMessage = mInputPanel.getText().toString();

                if (!mMessage.isEmpty()) {
                    sendMessage(mMessage);
                }
                break;

            default:
                break;
        }
    }

    /**
     * Compose & sends message to recipient
     * @param message that will be send to recipient
     */
    private void sendMessage(String message) {
        if (mBound) {
            Date currentDate = new Date();
            long currentDateMillis = currentDate.getTime();

            WebSocketMessage webSocketMessage = new WebSocketOutgoingMessage.Builder()
                    .body(message)
                    .dateSent(currentDateMillis)
                    .dateReceived(currentDateMillis)
                    .recipient(mRecipient)
                    .build();

            Message msg = Message.obtain();
                msg.obj = webSocketMessage;
                msg.what = MessageService.SEND_MESSAGE_ACTION;
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
