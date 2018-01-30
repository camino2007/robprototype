package com.rxdroid.repository.cache;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;

public abstract class TimeLimitedCache<T> implements Cache<T> {

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
        this.savedTimeStamp = getCurrentTimeStamp();
    }

    private long getCurrentTimeStamp() {
        return System.currentTimeMillis();
    }

    public boolean isCachedTimingValid() {
        final long currentTimeStamp = getCurrentTimeStamp();
        return currentTimeStamp < savedTimeStamp + maxValidDuration;
    }

}
