package com.rxdroid.repository.repositories.detail

import android.text.TextUtils
import android.util.Log
import com.rxdroid.api.error.RequestError
import com.rxdroid.api.github.provider.GitHubRepositoryProvider
import com.rxdroid.repository.model.Repository
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.Status
import com.rxdroid.repository.model.User
import fup.prototype.data.details.RepositoryDatabaseProvider
import fup.prototype.data.details.RepositoryDto
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsUiRepository @Inject constructor(
        val gitHubRepositoryProvider: GitHubRepositoryProvider,
        val repositoryDatabaseProvider: RepositoryDatabaseProvider) : DetailRepository {


    private object Constants {
        const val TAG: String = "DetailsUiRepository"
    }

    private var listResource: Resource<ArrayList<Repository>>? = null
    private var lastSearchValue: String? = null

    override val cachedValue: Resource<ArrayList<Repository>>?
        get() = listResource


    override fun loadReposForUser(user: User): Observable<Resource<ArrayList<Repository>>> {
        if (hasValidCacheValue(user.login)) {
            listResource = Resource.success(listResource?.data!!)
            return Observable.just(listResource)
        }
        lastSearchValue = user.login
        return gitHubRepositoryProvider.loadBySearchValue(user.login)
                .map({ listResponse ->
                    if (listResponse.isSuccessful) {
                        val repositories = Repository.fromApiList(listResponse.body())
                        listResource = Resource.success(repositories)
                    } else {
                        val requestError = RequestError.create(listResponse, null)
                        listResource = Resource.error(requestError, null)
                    }
                    return@map listResource!!
                })
                .doOnNext({ listResource ->
                    if (listResource.status == Status.SUCCESS) {
                        updateDatabase(listResource.data!!, user.id)
                                .subscribeOn(Schedulers.io())
                                .subscribe(DatabaseWriteObserver())
                    }
                })
    }


    fun hasValidCacheValue(currentSearchValue: String): Boolean {
        return listResource != null && listResource?.data != null && TextUtils.equals(lastSearchValue, currentSearchValue)
    }

    private fun updateDatabase(repositories: ArrayList<Repository>, userId: Int): Completable {
        val repositoryDtos: ArrayList<RepositoryDto> = ArrayList(repositories.size)
        var dto: RepositoryDto
        repositories.let {
            for (repo in repositories) {
                dto = RepositoryDto()
                dto.name = repo.name
                dto.githubUserId = userId
                dto.fullName = repo.fullName
                dto.idRep = repo.id
                repositoryDtos.add(dto)
            }
        }
        return repositoryDatabaseProvider.insertOrUpdate(repositoryDtos)
    }

    inner class DatabaseWriteObserver : DisposableCompletableObserver() {

        override fun onComplete() {
            Log.d(Constants.TAG, "DatabaseWriteObserver - onComplete")
        }

        override fun onError(e: Throwable) {
            Log.e(Constants.TAG, "DatabaseWriteObserver - onError: ", e)
        }

    }
}