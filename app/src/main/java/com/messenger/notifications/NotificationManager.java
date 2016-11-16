package com.messenger.notifications;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.messenger.R;


/**
 * @author equals on 14.11.16.
 */
public class NotificationManager {

    public static void showNotification(Context context, String title, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.ic_notification_icon);
            builder.setContentTitle(title);
            builder.setContentText(text);
            builder.setWhen(System.currentTimeMillis());
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
            builder.setAutoCancel(true);

        Notification notification = builder.build();
        ((android.app.NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(31337, notification);
    }

}
