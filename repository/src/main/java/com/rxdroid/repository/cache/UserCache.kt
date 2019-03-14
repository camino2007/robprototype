package com.rxdroid.repository.cache

import com.rxdroid.repository.model.User
import java.util.concurrent.TimeUnit

class UserCache(timeOut: Long, timeUnit: TimeUnit) : TimeLimitedCache<User>(timeOut, timeUnit) {

    override fun getData(): User = getData()

    override fun hasValidCachedData() = isTimingValid()

    override fun isSameObjectCached(t: User?) = getData() == t

}
