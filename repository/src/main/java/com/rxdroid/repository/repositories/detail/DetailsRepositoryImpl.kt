package com.rxdroid.repository.repositories.detail

import com.rxdroid.api.github.details.DetailsProviderImpl
import com.rxdroid.data.details.RepositoryDatabaseProvider
import com.rxdroid.repository.model.Repository
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.User
import io.reactivex.Single

class DetailsRepositoryImpl(private val searchApiProvider: DetailsProviderImpl,
                            private val repositoryDatabaseProvider: RepositoryDatabaseProvider) : DetailsRepository {

    override fun loadRepositoriesForUser(user: User): Single<Resource<List<Repository>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}