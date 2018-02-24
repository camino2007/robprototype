package com.rxdroid.repository.cache

interface Cache<T> {

    fun getData(): T?

    fun setData(t: T)

    fun hasValidCachedData(): Boolean

    fun isSameObjectCached(t: T?): Boolean

}
