package com.rxdroid.repository.repositories.detail

import android.text.TextUtils
import com.rxdroid.api.error.RequestError
import com.rxdroid.api.github.details.DetailsApiProvider
import com.rxdroid.api.github.model.GitHubRepoData
import com.rxdroid.data.details.RepositoryDatabaseProvider
import com.rxdroid.data.details.UserRepositoryDto
import com.rxdroid.repository.cache.RepositoryListCache
import com.rxdroid.repository.model.Repository
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.Status
import com.rxdroid.repository.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.TimeUnit

class DetailsRepositoryImpl(private val searchApiProvider: DetailsApiProvider,
                            private val repositoryDatabaseProvider: RepositoryDatabaseProvider) : DetailsRepository {

    private var lastSearchValue: String? = null

    private val repositoryCache = RepositoryListCache(10L, TimeUnit.SECONDS)

    override fun loadRepositoriesForUser(user: User): Observable<Resource<List<Repository>>> {
        if (hasValidCacheValue(user.login)) {
            val repositories = repositoryCache.getData()
            repositories?.let {
                val successResource = Resource.success(it)
                return Observable.just(successResource)
            }
        }
        lastSearchValue = user.login
        return searchApiProvider
                .getRepositoriesForUser(user.login)
                .toObservable()
                .compose(getResponseTransformer())
                .doOnNext(getCacheConsumer())
                .doOnNext(getDatabaseConsumer(user.id))
    }

    private fun getResponseTransformer(): ObservableTransformer<Response<List<GitHubRepoData>>, Resource<List<Repository>>> {
        return ObservableTransformer { responseObservable ->
            responseObservable.map { response ->
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        return@map Resource.success(Repository.fromApiList(result))
                    }
                }
                val requestError = RequestError.create(response)
                Resource.error<List<Repository>>(requestError)
            }
        }
    }

    private fun getCacheConsumer(): Consumer<in Resource<List<Repository>>>? {
        return Consumer { repositoriesResource ->
            if (repositoriesResource.status == Status.SUCCESS) {
                repositoriesResource.data?.let { repositories ->
                    repositoryCache.setData(repositories)
                }
            }
        }
    }

    private fun getDatabaseConsumer(userId: Int): Consumer<in Resource<List<Repository>>>? {
        return Consumer { repositoriesResource ->
            if (repositoriesResource.status == Status.SUCCESS) {
                repositoriesResource.data?.let { repositories ->
                    updateDatabase(repositories, userId)
                            .subscribeOn(Schedulers.io())
                            .subscribe({
                                Timber.i("Write to db successful")
                            }, { error: Throwable ->
                                Timber.e(error, "Write to db failed")
                            })
                }
            }
        }
    }

    private fun hasValidCacheValue(currentSearchValue: String?) = TextUtils.equals(lastSearchValue, currentSearchValue) && repositoryCache.hasValidCachedData()


    private fun updateDatabase(repositories: List<Repository>, userId: Int): Completable {
        return getUpdateUserCompletable(repositories, userId)
                .andThen(getUpdateRepositoryCompletable(repositories, userId))
    }

    private fun getUpdateUserCompletable(repositories: List<Repository>, userId: Int): Completable {
        val repositoryDtos: ArrayList<UserRepositoryDto> = ArrayList(repositories.size)
        var dto: UserRepositoryDto
        for (repo in repositories) {
            dto = UserRepositoryDto(fullName = repo.fullName, name = repo.name, githubUserId = userId, idRep = repo.repositoryId)
            repositoryDtos.add(dto)
        }
        return repositoryDatabaseProvider.insertOrUpdate(repositoryDtos)
    }

    private fun getUpdateRepositoryCompletable(repositories: List<Repository>, userId: Int): Completable {
        val repositoryDtos: ArrayList<UserRepositoryDto> = ArrayList(repositories.size)
        var dto: UserRepositoryDto
        for (repo in repositories) {
            dto = UserRepositoryDto(fullName = repo.fullName, name = repo.name, githubUserId = userId, idRep = repo.repositoryId)
            repositoryDtos.add(dto)
        }
        return repositoryDatabaseProvider.insertOrUpdate(repositoryDtos)
    }

}
