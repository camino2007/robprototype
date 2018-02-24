package com.rxdroid.api.github.provider

import io.reactivex.Observable
import retrofit2.Response

interface ApiProvider<T> {

    fun loadBySearchValue(searchValue: String): Observable<Response<T>>
}
