package com.rxdroid.repository.cache;

import com.rxdroid.repository.model.User;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;

public class UserCache extends TimeLimitedCache<User> {

    private User user;

    public UserCache(@NonNull final long timeOut, @NonNull final TimeUnit timeUnit) {
        super(timeOut, timeUnit);
    }

    @Override
    public boolean isSameObjectCached(final User user) {
        return getData().equals(user);
    }

    @Override
    public void setData(final User data) {
        super.setData(data);
        this.user = data;
    }

    @Override
    public User getData() {
        return user;
    }

    @Override
    public boolean hasValidCachedData() {
        return getData() != null && isCachedTimingValid();
    }
}
