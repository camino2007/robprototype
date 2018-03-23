package com.rxdroid.repository.repositories.search

import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.User
import com.rxdroid.repository.repositories.UiRepository
import io.reactivex.Observable

interface SearchRepository : UiRepository<User> {

    fun searchForUser(searchValue: String): Observable<Resource<User>>

}