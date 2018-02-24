package com.rxdroid.repository.cache

import java.util.concurrent.TimeUnit

abstract class TimeLimitedCache<T>(timeOut: Long, timeUnit: TimeUnit) : Cache<T> {

    var savedTimeStamp: Long = -1L
    var maxValidDuration: Long = -1L

    init {
        this.maxValidDuration = TimeUnit.MILLISECONDS.convert(timeOut, timeUnit)
    }

    override fun setData(t: T) {
        this.savedTimeStamp = getCurrentTimeStamp()
    }

    private fun getCurrentTimeStamp(): Long {
        return System.currentTimeMillis()
    }

    fun isTimingValid(): Boolean {
        return getCurrentTimeStamp() < savedTimeStamp + maxValidDuration
    }
}