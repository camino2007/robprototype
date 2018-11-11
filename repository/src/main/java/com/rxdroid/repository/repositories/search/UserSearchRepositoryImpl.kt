package com.rxdroid.repository.repositories.search

import android.text.TextUtils
import android.util.Log
import com.rxdroid.api.error.RequestError
import com.rxdroid.api.github.model.GitHubUserData
import com.rxdroid.api.github.search.SearchApiProvider
import com.rxdroid.data.search.UserDatabaseProvider
import com.rxdroid.data.search.UserDto
import com.rxdroid.repository.cache.UserCache
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.observers.DisposableCompletableObserver
import retrofit2.Response
import java.util.concurrent.TimeUnit

class UserSearchRepositoryImpl(private val searchApiProvider: SearchApiProvider,
                               private val userDatabaseProvider: UserDatabaseProvider) : UserSearchRepository {


    private object Constants {
        const val TAG: String = "UserUiRepository"
    }

    private val userCache: UserCache = UserCache(10L, TimeUnit.SECONDS)

    private var lastSearchValue: String? = null
    private var userResource: Resource<User>? = null

    override fun searchForUser(searchValue: String): Observable<Resource<User>> {
        if (TextUtils.isEmpty(searchValue)) {
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
    }

/*    override fun searchForUser(searchValue: String): Single<Resource<User>> {
        if (TextUtils.isEmpty(searchValue)) {
            userResource = Resource.error(RequestError.create(RequestError.ERROR_CODE_NO_SEARCH_INPUT), null)
            return Single.just(userResource)
        }
        if (hasValidCacheValue(searchValue)) {
            userResource = Resource.success(userResource?.data!!)
            return Single.just(userResource)
        }
        this.lastSearchValue = searchValue
        return searchApiProvider.findUserBySearchValue(searchValue)
                .map { userDataResponse ->
                    userResource = if (userDataResponse.isSuccessful) {
                        val user = User.fromApi(userDataResponse.body())
                        userCache.setData(user)
                        Resource.success(user)
                    } else {
                        val requestError = RequestError.create(userDataResponse)
                        Resource.error(requestError, null)
                    }
                    return@map userResource!!
                }
                .doOnEvent { t1, t2 ->
                    {
                       if (userResource.status == Status.SUCCESS) {
                              updateDatabase(userResource.data!!)
                                      .subscribeOn(Schedulers.io())
                                      .subscribe(DatabaseWriteObserver())
                          }
                    }
                }
    }*/

    private fun hasValidCacheValue(currentSearchValue: String): Boolean {
        return userResource != null && userResource?.data != null
                && TextUtils.equals(lastSearchValue, currentSearchValue) && userCache.hasValidCachedData()
    }

    private fun updateDatabase(user: User): Completable {
        Log.d(Constants.TAG, "updateDatabase")
        val userDto = UserDto(name = user.name,
                login = user.login,
                publicRepoCount = user.publicRepoCount,
                publicGistCount = user.publicGistCount,
                githubUserId = user.id)
        return userDatabaseProvider.insertOrUpdate(userDto)
    }


    inner class DatabaseWriteObserver : DisposableCompletableObserver() {

        override fun onComplete() {
            // In case you want to know, when db write succeeded
            Log.d(Constants.TAG, "DatabaseWriteObserver - onComplete")
        }

        override fun onError(e: Throwable) {
            //Database write transaction failed due of reasons ...
            Log.e(Constants.TAG, "DatabaseWriteObserver - onError: ", e)
        }

    }

}