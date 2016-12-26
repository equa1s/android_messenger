package com.messenger.database;

import android.content.Context;

import com.messenger.database.model.DaoMaster;
import com.messenger.database.model.DaoSession;
import com.messenger.database.model.MessageEntity;
import com.messenger.database.model.MessageEntityDao;
import com.messenger.database.model.ThreadEntity;
import com.messenger.database.model.ThreadEntityDao;
import com.messenger.database.model.UserEntityDao;
import com.messenger.database.pojo.WebSocketMessages;
import com.messenger.database.pojo.WebSocketIncomingMessage;
import com.messenger.database.pojo.WebSocketMessage;
import com.messenger.database.pojo.WebSocketOutgoingMessage;

import java.util.ArrayList;
import java.util.List;

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

    public MessageEntity readIncomingMessage(WebSocketIncomingMessage webSocketIncomingMessage) {
        return new MessageReader(webSocketIncomingMessage).getIncomingMessage();
    }

    public MessageEntity readOutgoingMessage(WebSocketMessage webSocketMessage) {
        return new MessageReader(webSocketMessage).getOutgoingMessage();
    }

    public List<MessageEntity> readIncomingMessages(WebSocketMessages webSocketMessages) {
        return new MessageReader(webSocketMessages).getIncomingMessages();
    }

    /**
     * Update thread by message id.
     *
     * @param messageEntity Object need for thread_id, last_message, sent_time.
     */
    public void updateThread(MessageEntity messageEntity) {
        ThreadEntity staleThreadEntity = getThreadEntityDao().load(messageEntity.getThreadId());

        ThreadEntity updatedThreadEntity = new ThreadEntity.Builder()
                .threadId(staleThreadEntity.getThreadId())
                .lastMessage(messageEntity.getBody())
                .userId(staleThreadEntity.getUserId())
                .lastMessageDate(messageEntity.getSentTime())
                .build();

        getThreadEntityDao().update(updatedThreadEntity);
    }

    /**
     * Insert message entity to messages table.
     *
     * @param messageEntity Entity which will be inserted
     * @return row ID of newly inserted entity
     */
    public long insertMessage(MessageEntity messageEntity) {
        return getMessageEntityDao().insert(messageEntity);
    }

    private class MessageReader {

        private WebSocketMessage webSocketMessage;
        private WebSocketMessages webSocketMessages;
        private WebSocketIncomingMessage webSocketIncomingMessage;

        MessageReader(WebSocketIncomingMessage webSocketIncomingMessage) {
            this.webSocketIncomingMessage = webSocketIncomingMessage;
        }

        MessageReader(WebSocketMessage webSocketMessage) {
            this.webSocketMessage = webSocketMessage;
        }

        MessageReader(WebSocketMessages webSocketMessages) {
            this.webSocketMessages = webSocketMessages;
        }

        MessageEntity getIncomingMessage() {

            ThreadEntity threadEntity = mThreadEntityDao
                    .queryBuilder()
                    .where(ThreadEntityDao.Properties.UserId.eq(webSocketIncomingMessage.getSender()))
                    .unique();

            return new MessageEntity.Builder()
                    .body(webSocketIncomingMessage.getBody())
                    .from(webSocketIncomingMessage.getSender())
                    .receivedTime(webSocketIncomingMessage.getDateReceived())
                    .sentTime(webSocketIncomingMessage.getDateSent())
                    .type(MessageEntity.INCOMING)
                    .threadId(threadEntity.getThreadId())
                    .build();
        }

        MessageEntity getOutgoingMessage() {

            WebSocketOutgoingMessage outgoingMessage =
                    (WebSocketOutgoingMessage) webSocketMessage.getWebSocketData();

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

        List<MessageEntity> getIncomingMessages() {

            List<WebSocketIncomingMessage> incomingMessages = webSocketMessages.getMessages();

            if (incomingMessages != null && !incomingMessages.isEmpty()) {
                ThreadEntity threadEntity = mThreadEntityDao
                        .queryBuilder()
                        .where(ThreadEntityDao.Properties.UserId.eq(incomingMessages.get(0).getSender()))
                        .unique();

                List<MessageEntity> messageEntities = new ArrayList<>();
                for (WebSocketIncomingMessage message : incomingMessages) {
                    messageEntities.add(new MessageEntity.Builder()
                            .body(message.getBody())
                            .from(message.getSender())
                            .receivedTime(message.getDateReceived())
                            .sentTime(message.getDateSent())
                            .threadId(threadEntity.getThreadId())
                            .type(MessageEntity.INCOMING)
                            .build());
                }
                return messageEntities;
            }
            return null;
        }

    }
}
