package com.messenger.util;

import android.graphics.Color;

import java.util.Random;

/**
 * @author equals on 15.11.16.
 */
public class ColorUtil {

    private static Random random;

    static {
        random = new Random();
    }

    public static int getRandomColor() {
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

}
