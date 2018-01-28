package com.rxdroid.repository.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.rxdroid.api.RequestError;

@AutoValue
public abstract class UserResponse {

    @Nullable
    public abstract User getUser();

    @Nullable
    public abstract RequestError getRequestError();

    @NonNull
    public static UserResponse create(@Nullable User user, @Nullable RequestError requestError) {
        return new AutoValue_UserResponse(user, requestError);
    }
}
