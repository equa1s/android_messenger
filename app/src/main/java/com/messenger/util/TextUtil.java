package com.messenger.util;

import com.amulyakhare.textdrawable.TextDrawable;

/**
 * @author equals on 22.11.16.
 */
public class TextUtil {

    private static String getCharAtIndex(int index, String input) {
        return String.valueOf(input.charAt(index));
    }
    public static TextDrawable getTextDrawable(String input, Integer color) {
        String mTitleLetter = getCharAtIndex(0, input);
        if (color != null) {
            return TextDrawable.builder()
                    .beginConfig()
                    .toUpperCase()
                    .bold()
                    .endConfig()
                    .buildRound(TextUtil.getCharAtIndex(0, mTitleLetter), color);
        }
        return TextDrawable.builder()
                .beginConfig()
                .toUpperCase()
                .bold()
                .endConfig()
                .buildRound(TextUtil.getCharAtIndex(0, mTitleLetter), ColorUtil.getRandomColor());
    }
}
