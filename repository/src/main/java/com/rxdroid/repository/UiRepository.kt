package com.rxdroid.repository

import com.rxdroid.repository.model.Resource

import io.reactivex.Observable

interface UiRepository<T> {

    val cachedValue: Resource<T>?

    fun loadBySearchValue(searchValue: String): Observable<Resource<T>>

}
