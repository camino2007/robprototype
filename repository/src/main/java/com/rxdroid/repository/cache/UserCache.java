package com.rxdroid.repository.cache;

import com.rxdroid.repository.model.User;
import io.reactivex.annotations.NonNull;
import java.util.concurrent.TimeUnit;

public class UserCache extends TimeLimitedCache<User> {

    public UserCache(@NonNull final long timeOut, @NonNull final TimeUnit timeUnit) {
        super(timeOut, timeUnit);
    }

    @Override
    public boolean isSameObjectCached(final User user) {
        return hasCachedData() && getData().equals(user);
    }
}
