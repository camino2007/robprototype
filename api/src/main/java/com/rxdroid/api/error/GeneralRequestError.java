package com.rxdroid.api.error;

import android.support.annotation.Nullable;

import retrofit2.Response;

public class GeneralRequestError extends RequestError {

    public GeneralRequestError(@Nullable Response<?> response, @Nullable Throwable throwable) {
        super(response, throwable, -1);
    }
}
