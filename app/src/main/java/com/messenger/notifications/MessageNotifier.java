package com.messenger.notifications;



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
