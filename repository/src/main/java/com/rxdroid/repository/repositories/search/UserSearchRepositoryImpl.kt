package com.rxdroid.repository.repositories.search

import com.rxdroid.api.github.search.SearchApiProvider
import com.rxdroid.data.details.RepositoryDatabaseProvider
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.User
import io.reactivex.Single

class UserSearchRepositoryImpl(val searchApiProvider: SearchApiProvider,
                               val repositoryDatabaseProvider: RepositoryDatabaseProvider) : UserSearchRepository {

    override fun searchForUser(searchValue: String): Single<Resource<User>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}