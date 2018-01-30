package com.rxdroid.repository.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rxdroid.api.RequestError;


public class UserResponse {

    @Nullable
    private User user;

    @Nullable
    private RequestError requestError;

    public UserResponse(@Nullable final User user, @Nullable final RequestError requestError) {
        this.user = user;
        this.requestError = requestError;
    }

    @Nullable
    public User getUser() {
        return user;
    }

    @Nullable
    public RequestError getRequestError() {
        return requestError;
    }

    public boolean hasError() {
        return requestError != null;
    }

    @NonNull
    public static UserResponse create(@Nullable final User user, @Nullable final RequestError requestError) {
        return new UserResponse(user, requestError);
    }
}
