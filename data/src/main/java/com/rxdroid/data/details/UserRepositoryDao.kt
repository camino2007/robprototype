package com.rxdroid.data.details

import io.reactivex.Completable
import io.reactivex.Maybe

interface UserRepositoryDao {

    fun insertOrUpdate(userDtoRepositories: List<UserRepositoryDto>): Completable

    fun getRepositoriesForUserId(userId: Int): Maybe<List<UserRepositoryDto>>

}