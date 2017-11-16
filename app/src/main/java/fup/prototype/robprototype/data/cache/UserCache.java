package fup.prototype.robprototype.data.cache;

import fup.prototype.robprototype.model.User;
import io.reactivex.annotations.NonNull;
import java.util.concurrent.TimeUnit;

public class UserCache extends TimeLimitedCache<User> {

    public UserCache(@NonNull final long timeOut, @NonNull final TimeUnit timeUnit) {
        super(timeOut, timeUnit);
    }

    public boolean isSameUserCached(@NonNull final String userLogin) {
        return hasCachedData() && getData().getLogin().equalsIgnoreCase(userLogin);
    }
}
