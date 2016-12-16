package com.messenger.util;

import android.content.Context;
import android.os.Vibrator;

/**
 *
 * @author equals on 13.12.16.
 */
public class SystemUtils {
    public static void vibrate(Context context, long millis) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(millis);
    }
}
