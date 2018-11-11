package com.rxdroid.repository.repositories.search

import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.User
import io.reactivex.Observable
import io.reactivex.Single

interface UserSearchRepository {

    fun searchForUser(searchValue: String): Observable<Resource<User>>

}