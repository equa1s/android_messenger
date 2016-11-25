package com.messenger.notifications;



public class MessageNotifier {

    private volatile static long visibleThread = -1;

    private MessageNotifier() {
    }

    public static void setVisibleThread(long threadId) {
        visibleThread = threadId;
    }

    public static boolean isVisibleThread(long threadId) {
        return visibleThread == threadId;
    }
}
