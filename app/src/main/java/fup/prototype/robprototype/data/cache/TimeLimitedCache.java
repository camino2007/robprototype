package fup.prototype.robprototype.data.cache;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;

public abstract class TimeLimitedCache<T> extends Cache<T> {

    private long savedTimeStamp = -1L;

    /**
     * defines, for how long a stored object is valid
     */
    private long maxValidDuration = -1L;

    TimeLimitedCache(@NonNull final long timeOut, @NonNull final TimeUnit timeUnit) {
        this.maxValidDuration = TimeUnit.MILLISECONDS.convert(timeOut, timeUnit);
    }

    @Override
    public void setData(T data) {
        super.setData(data);
        this.savedTimeStamp = getCurrentTimeStamp();
    }

    private long getCurrentTimeStamp() {
        return System.currentTimeMillis();
    }

    public boolean isCacheValid() {
        final long currentTimeStamp = getCurrentTimeStamp();
        return currentTimeStamp < savedTimeStamp + maxValidDuration;
    }

}
