package com.rxdroid.api.error;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.ConnectException;
import java.net.UnknownHostException;

import retrofit2.Response;

public abstract class RequestError {

    public static final int ERROR_CODE_NO_SEARCH_INPUT = 102;
    public static final int ERROR_CODE_NO_RESULTS = 103;

    @Nullable
    private final Response<?> response;

    @Nullable
    private final Throwable throwable;

    /**
     * Can be used to create a custom, non-Api related error.
     */
    private int errorCode;

    protected RequestError(@Nullable Response<?> response, @Nullable Throwable throwable, final int errorCode) {
        this.response = response;
        this.throwable = throwable;
        this.errorCode = errorCode;
    }

    @NonNull
    public static RequestError create(@Nullable final Response<?> response, @Nullable final Throwable throwable) {
        if (throwable != null) {
            // handle some other important exceptions when no response is available

            if (throwable instanceof ConnectException || throwable instanceof UnknownHostException) {
                return new NoConnectionRequestError(throwable);
            }
        }

        return new GeneralRequestError(response, throwable);
    }

    @NonNull
    public static RequestError create(final int errorCode) {
        return new CustomRequestError(errorCode);
    }

    @Nullable
    public Response<?> getResponse() {
        return response;
    }

    @Nullable
    public Throwable getThrowable() {
        return throwable;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
