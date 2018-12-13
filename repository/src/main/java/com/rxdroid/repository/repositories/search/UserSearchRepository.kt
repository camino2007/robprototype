package com.rxdroid.repository.repositories.search

import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.User
import io.reactivex.Flowable
import io.reactivex.Observable

interface UserSearchRepository {

    fun searchForUser(searchValue: String): Flowable<Resource<User>>

}