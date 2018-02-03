package com.rxdroid.repository.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rxdroid.api.error.RequestError;

public class Resource<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final RequestError requestError;

    public Resource(@NonNull Status status, @Nullable T data, @Nullable RequestError requestError) {
        this.status = status;
        this.data = data;
        this.requestError = requestError;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(RequestError requestError, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, requestError);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }
}


