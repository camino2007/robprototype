package com.rxdroid.data.search

import io.reactivex.Completable
import io.reactivex.Maybe

interface UserDao {

    fun insertOrUpdate(userDto: UserDto): Completable

    fun getUserForSearchValue(searchValue: String): Maybe<UserDto>

}