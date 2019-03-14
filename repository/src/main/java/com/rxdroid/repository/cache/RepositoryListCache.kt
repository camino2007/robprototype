package com.rxdroid.repository.cache

import com.rxdroid.repository.model.Repository
import java.util.concurrent.TimeUnit

class RepositoryListCache(timeOut: Long, timeUnit: TimeUnit) : TimeLimitedCache<List<Repository>>(timeOut, timeUnit) {

    override fun getData(): List<Repository> = getData()

    override fun hasValidCachedData() = isTimingValid()

    override fun isSameObjectCached(t: List<Repository>?): Boolean = getData() == t
}
