package com.rxdroid.api.error;


import android.support.annotation.Nullable;

public class NoConnectionRequestError extends RequestError {

    public NoConnectionRequestError(@Nullable Throwable throwable) {
        super(null, throwable, -1);
    }
}
