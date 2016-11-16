package com.messenger.api.exception;

import java.io.IOException;

/**
 * @author equals on 14.11.16.
 */
public class RetrofitFailureException extends IOException {
    public RetrofitFailureException(String message) {
        super(message);
    }
}
