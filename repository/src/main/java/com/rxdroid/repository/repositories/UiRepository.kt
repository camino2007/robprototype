package com.rxdroid.repository.repositories

import com.rxdroid.repository.model.Resource

interface UiRepository<out T> {

    val cachedValue: Resource<T>?

}
