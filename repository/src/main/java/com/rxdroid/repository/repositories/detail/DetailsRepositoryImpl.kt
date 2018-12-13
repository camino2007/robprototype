package com.rxdroid.repository.repositories.detail

import android.text.TextUtils
import com.rxdroid.api.error.RequestError
import com.rxdroid.api.github.details.DetailsApiProvider
import com.rxdroid.data.details.RepositoryDatabaseProvider
import com.rxdroid.data.details.UserRepositoryDto
import com.rxdroid.repository.model.Repository
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.Status
import com.rxdroid.repository.model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class DetailsRepositoryImpl(private val searchApiProvider: DetailsApiProvider,
                            private val repositoryDatabaseProvider: RepositoryDatabaseProvider) : DetailsRepository {

    private var listResource: Resource<List<Repository>>? = null
    private var lastSearchValue: String? = null

    override fun loadRepositoriesForUser(user: User): Flowable<Resource<List<Repository>>> {
        if (hasValidCacheValue(user.login)) {
            return Flowable.just(Resource.success(listResource?.data!!))
        }
        lastSearchValue = user.login
        return searchApiProvider.getRepositoriesForUser(user.login).toFlowable()
                .flatMap<Resource<List<Repository>>> { listResponse ->
                    listResource = if (listResponse.isSuccessful) {
                        val repositories = Repository.fromApiList(listResponse.body())
                        Resource.success(repositories)
                    } else {
                        val requestError = RequestError.create(listResponse)
                        Resource.error(requestError)
                    }
                    Flowable.just(listResource)
                }
                .doOnNext {
                    if (it.status == Status.SUCCESS) {
                        updateDatabase(it.data!!, user.id)
                                .subscribeOn(Schedulers.io())
                                .subscribe({
                                    Timber.i("Write to db successful")
                                }, { error: Throwable ->
                                    Timber.e(error, "Write to db failed")
                                })
                    }
                }
    }

    private fun hasValidCacheValue(currentSearchValue: String): Boolean {
        return listResource != null && listResource?.data != null && TextUtils.equals(lastSearchValue, currentSearchValue)
    }

    private fun updateDatabase(repositories: List<Repository>, userId: Int): Completable {
        val repositoryDtos: ArrayList<UserRepositoryDto> = ArrayList(repositories.size)
        var dto: UserRepositoryDto
        repositories.let {
            for (repo in repositories) {
                dto = UserRepositoryDto(fullName = repo.fullName, name = repo.name, githubUserId = userId, idRep = repo.repositoryId)
                repositoryDtos.add(dto)
            }
        }
        return repositoryDatabaseProvider.insertOrUpdate(repositoryDtos)
    }

}

