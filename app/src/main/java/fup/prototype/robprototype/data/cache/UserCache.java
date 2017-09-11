package fup.prototype.robprototype.data.cache;


import java.util.concurrent.TimeUnit;

import fup.prototype.robprototype.model.User;
import io.reactivex.annotations.NonNull;

public class UserCache extends TimeLimitedCache<User> {

    public UserCache(@NonNull final long timeOut, @NonNull final TimeUnit timeUnit) {
        super(timeOut, timeUnit);
    }

    public boolean isSameUserCached(@NonNull final String userName) {
        return hasCachedData() && getData().getName().equalsIgnoreCase(userName);
    }
}
