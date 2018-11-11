package com.rxdroid.data.details

import io.reactivex.Completable
import io.reactivex.Maybe

interface RepositoryDatabaseProvider {

    fun insertOrUpdate(userDtoRepositories: List<UserRepositoryDto>): Completable

    fun getRepositoriesForUserId(userId: Int): Maybe<List<UserRepositoryDto>>

}