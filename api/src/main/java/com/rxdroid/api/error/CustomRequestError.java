package com.rxdroid.api.error;

public class CustomRequestError extends RequestError {

    protected CustomRequestError(int errorCode) {
        super(null, null, errorCode);
    }
}
