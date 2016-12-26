package com.messenger.notifications;


/**
 * Simple class to hold visibility of current thread.
 * Need to handle notifications.
 *
 * @author equals on 14.11.16.
 */
public class MessageNotifier {

    public static final Long UNKNOWN = -1L;

    private volatile static Long visibleThread = UNKNOWN;

    private MessageNotifier() {
    }

    public static void setVisibleThread(Long threadId) {
        visibleThread = threadId;
    }

    public static boolean isVisibleThread(Long threadId) {
        return visibleThread.equals(threadId);
    }

}
