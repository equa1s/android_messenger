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
import android.util.Log;

import com.messenger.database.model.DaoSession;
import com.messenger.database.pojo.WebSocketMessage;
import com.messenger.events.MessageEvent;
import com.messenger.preferences.MessengerSharedPreferences;
import com.messenger.service.MessageRetrievalService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import butterknife.OnClick;


/**
 * @author equals on 15.11.16.
 */
public class ConversationActivity extends BaseActivity {

    private static final String TAG = ConversationActivity.class.getSimpleName();

    public static final String USER_LOGIN = "user_login";
    private Messenger mMessageRetrievalService;
    private String userLogin;
    boolean mBound = false;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        bindService(new Intent(this, MessageRetrievalService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPreCreate(DaoSession daoSession) {

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

    }

    @Override
    protected int setLayout() {
        return R.layout.conversation_activity;
    }

    @Override
    protected void onPostCreate() {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {

    }
    /**
     * TODO: bind {@link android.widget.Button} to this method
     * - add message to db
     * - redraw adapter
     * @param message
     */
    @OnClick()
    private void sendMessage(String message) {
        if (mBound) {
            Date currentDate = new Date();
            long currentDateMillis = currentDate.getTime();

            WebSocketMessage webSocketMessage = new WebSocketMessage.Builder()
                    .body(message)
                    .dateSent(currentDateMillis)
                    .dateReceived(currentDateMillis)
                    .sender(MessengerSharedPreferences.getUserLogin(this))
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
