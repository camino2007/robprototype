package com.rxdroid.repository.repositories.detail

import com.rxdroid.repository.model.Repository
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.User
import io.reactivex.Single

interface DetailsRepository {

    fun loadRepositoriesForUser(user: User): Single<Resource<List<Repository>>>

}