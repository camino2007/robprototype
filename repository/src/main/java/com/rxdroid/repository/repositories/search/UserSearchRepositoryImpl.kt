package com.rxdroid.repository.repositories.search

import android.text.TextUtils
import com.rxdroid.api.error.RequestError
import com.rxdroid.api.github.model.GitHubUserData
import com.rxdroid.api.github.search.SearchApiProvider
import com.rxdroid.data.search.UserDatabaseProvider
import com.rxdroid.data.search.UserDto
import com.rxdroid.repository.cache.UserCache
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

class UserSearchRepositoryImpl(private val searchApiProvider: SearchApiProvider,
                               private val userDatabaseProvider: UserDatabaseProvider) : UserSearchRepository {

    private val userCache: UserCache = UserCache(10L, TimeUnit.SECONDS)

    private var lastSearchValue: String? = null

    override fun searchForUser(searchValue: String): Observable<Resource<User>> {
        if (hasValidCacheValue(searchValue)) {
            val successResource = Resource.success(userCache.getData())
            return Observable.just(successResource)
        }
        lastSearchValue = searchValue
        return searchApiProvider
                .findUserBySearchValue(searchValue)
                .toObservable()
                .compose(getResponseTransformer())
                .doOnNext(getCacheConsumer())
                .doOnNext(getDatabaseConsumer())
    }

    private fun hasValidCacheValue(currentSearchValue: String) = TextUtils.equals(lastSearchValue, currentSearchValue) && userCache.hasValidCachedData()


    private fun updateDatabase(user: User): Completable {
        val userDto = UserDto(name = user.name,
                login = user.login,
                publicRepoCount = user.publicRepoCount,
                publicGistCount = user.publicGistCount,
                githubUserId = user.id)
        return userDatabaseProvider.insertOrUpdate(userDto)
    }

    private fun getCacheConsumer(): Consumer<in Resource<User>>? {
        return Consumer { userResource ->
            if (userResource.status == Status.SUCCESS) {
                userResource.data?.let { user ->
                    userCache.setData(user)
                }
            }
        }
    }

    private fun getDatabaseConsumer(): Consumer<in Resource<User>>? {
        return Consumer { userResource ->
            if (userResource.status == Status.SUCCESS) {
                userResource.data?.let { user ->
                    updateDatabase(user)
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

    private fun getResponseTransformer(): ObservableTransformer<Response<GitHubUserData>, Resource<User>> {
        return ObservableTransformer { responseObservable ->
            responseObservable.map { response ->
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        return@map Resource.success(User.fromApi(result))
                    }
                }
                val requestError = RequestError.create(response)
                Resource.error<User>(requestError)
            }
        }
    }

}


