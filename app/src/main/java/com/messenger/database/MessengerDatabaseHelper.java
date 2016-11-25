package com.messenger.database;

import android.content.Context;

import com.messenger.database.model.DaoMaster;
import com.messenger.database.model.DaoSession;
import com.messenger.database.model.MessageEntity;
import com.messenger.database.model.MessageEntityDao;
import com.messenger.database.model.ThreadEntity;
import com.messenger.database.model.ThreadEntityDao;
import com.messenger.database.model.UserEntityDao;
import com.messenger.database.pojo.WebSocketIncomingMessage;
import com.messenger.database.pojo.WebSocketMessage;
import com.messenger.database.pojo.WebSocketOutgoingMessage;

/**
 * Database helper
 *
 * @author equals on 10.11.16.
 */
public class MessengerDatabaseHelper extends DaoMaster.OpenHelper {

    private static final String DATABASE_NAME = "messenger-db";
    private static MessengerDatabaseHelper instance = null;
    private ThreadEntityDao mThreadEntityDao;
    private MessageEntityDao mMessageEntityDao;
    private UserEntityDao mUserEntityDao;

    public static MessengerDatabaseHelper getInstance(Context context) {
        synchronized (MessengerDatabaseHelper.class) {
            if (instance == null)
                instance = new MessengerDatabaseHelper(context, DATABASE_NAME);

            return instance;
        }
    }

    private MessengerDatabaseHelper(Context context, String name) {
        super(context, name);
        DaoSession mDaoSession = new DaoMaster(getWritableDb()).newSession();
        this.mMessageEntityDao = mDaoSession.getMessageEntityDao();
        this.mThreadEntityDao = mDaoSession.getThreadEntityDao();
        this.mUserEntityDao = mDaoSession.getUserEntityDao();
    }

    public ThreadEntityDao getThreadEntityDao() {
        return mThreadEntityDao;
    }

    public MessageEntityDao getMessageEntityDao() {
        return mMessageEntityDao;
    }

    public UserEntityDao getUserEntityDao() {
        return mUserEntityDao;
    }

    public MessageEntity readIncomingMessage(WebSocketMessage webSocketMessage) {
        return new MessageReader(webSocketMessage).getIncomingMessageEntity();
    }

    public MessageEntity readOutgoingMessage(WebSocketMessage webSocketMessage) {
        return new MessageReader(webSocketMessage).getOutgoingMessage();
    }

    private class MessageReader {

        private WebSocketMessage webSocketMessage;

        MessageReader(WebSocketMessage webSocketMessage) {
            this.webSocketMessage = webSocketMessage;
        }

        MessageEntity getIncomingMessageEntity() {

            WebSocketIncomingMessage incomingMessage = (WebSocketIncomingMessage) webSocketMessage;

            ThreadEntity threadEntity = mThreadEntityDao
                    .queryBuilder()
                    .where(ThreadEntityDao.Properties.UserId.eq(incomingMessage.getSender()))
                    .unique();

            return new MessageEntity.Builder()
                    .body(incomingMessage.getBody())
                    .from(incomingMessage.getSender())
                    .receivedTime(incomingMessage.getDateReceived())
                    .sentTime(incomingMessage.getDateSent())
                    .type(MessageEntity.INCOMING)
                    .threadId(threadEntity.getThreadId())
                    .build();
        }

        MessageEntity getOutgoingMessage() {

            WebSocketOutgoingMessage outgoingMessage = (WebSocketOutgoingMessage) webSocketMessage;

            ThreadEntity threadEntity = mThreadEntityDao
                    .queryBuilder()
                    .where(ThreadEntityDao.Properties.UserId.eq(outgoingMessage.getRecipient()))
                    .unique();

            return new MessageEntity.Builder()
                    .body(outgoingMessage.getBody())
                    .from(outgoingMessage.getRecipient())
                    .receivedTime(outgoingMessage.getDateReceived())
                    .sentTime(outgoingMessage.getDateSent())
                    .type(MessageEntity.OUTGOING)
                    .threadId(threadEntity.getThreadId())
                    .build();
        }

    }
}
