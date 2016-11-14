package com.messenger.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author equals on 11.11.16.
 */

public class Util {

    private static final String LOGIN_PATTERN = "[a-zA-Z_0-9]{6,16}";

    public static boolean isLoginValid(String login) {
        return login.matches(LOGIN_PATTERN);

    }

    public static String getSecret(int size) {
        byte[] secret = getSecretBytes(size);
        return Base64.encodeBytes(secret);
    }

    public static byte[] getSecretBytes(int size) {
        byte[] secret = new byte[size];
        getSecureRandom().nextBytes(secret);
        return secret;
    }

    public static SecureRandom getSecureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }
}
