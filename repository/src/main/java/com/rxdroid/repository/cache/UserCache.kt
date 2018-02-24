package com.rxdroid.repository.cache

import com.rxdroid.repository.model.User
import java.util.concurrent.TimeUnit

class UserCache(timeOut: Long, timeUnit: TimeUnit) : TimeLimitedCache<User>(timeOut, timeUnit) {


    override fun getData(): User {
        return getData()
    }

    override fun hasValidCachedData(): Boolean {
        return isTimingValid()
    }

    override fun isSameObjectCached(t: User?): Boolean {
        return getData() == t
    }


}