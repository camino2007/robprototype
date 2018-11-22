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
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.TimeUnit

class UserSearchRepositoryImpl(private val searchApiProvider: SearchApiProvider,
                               private val userDatabaseProvider: UserDatabaseProvider) : UserSearchRepository {

    private val userCache: UserCache = UserCache(10L, TimeUnit.SECONDS)

    private var lastSearchValue: String? = null
    private var userResource: Resource<User>? = null

    override fun searchForUser(searchValue: String): Observable<Resource<User>> {
        if (searchValue.isEmpty()) {
            userResource = Resource.error(RequestError.create(RequestError.ERROR_CODE_NO_SEARCH_INPUT))
            return Observable.just(userResource)
        }
        if (hasValidCacheValue(searchValue)) {
            userResource = Resource.success(userResource?.data!!)
            return Observable.just(userResource)
        }
        return searchApiProvider.findUserBySearchValue(searchValue).toObservable()
                .flatMap<Resource<User>> { userDataResponse: Response<GitHubUserData> ->
                    userResource = if (userDataResponse.isSuccessful) {
                        val user = User.fromApi(userDataResponse.body())
                        userCache.setData(user)
                        Resource.success(user)
                    } else {
                        val requestError = RequestError.create(userDataResponse)
                        Resource.error(requestError)
                    }
                    Observable.just(userResource)
                }
                .doOnNext {
                    if (it.status == Status.SUCCESS) {
                        updateDatabase(it.data)
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
        return userResource != null && userResource?.data != null
                && TextUtils.equals(lastSearchValue, currentSearchValue) && userCache.hasValidCachedData()
    }

    private fun updateDatabase(user: User?): Completable {
        user?.also {
            val userDto = UserDto(name = user.name,
                    login = user.login,
                    publicRepoCount = user.publicRepoCount,
                    publicGistCount = user.publicGistCount,
                    githubUserId = user.id)
            return userDatabaseProvider.insertOrUpdate(userDto)
        }
    }

}


