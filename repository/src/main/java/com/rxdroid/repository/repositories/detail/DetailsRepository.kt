package com.rxdroid.repository.repositories.detail

import com.rxdroid.repository.model.Repository
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.User
import io.reactivex.Flowable
import io.reactivex.Observable

interface DetailsRepository {

    fun loadRepositoriesForUser(user: User): Flowable<Resource<List<Repository>>>

}