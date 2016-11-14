package com.messenger.api.exception;

import java.io.IOException;

/**
 * @author equals on 10.11.16.
 */
public class NonSuccessfulResponseException extends IOException {
    public NonSuccessfulResponseException(String message) {
        super(message);
    }
}
