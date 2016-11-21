package com.messenger.notifications;



public class MessageNotifier {

    private static final String TAG = MessageNotifier.class.getSimpleName();

    public static final int NOTIFICATION_ID = 1338;
    private volatile static long visibleThread = -1;


    public static void setVisibleThread(long threadId) {
        visibleThread = threadId;
    }

    public static boolean isVisibleThread(long threadId) {
        return visibleThread == threadId;
    }
}
