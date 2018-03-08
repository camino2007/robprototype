package com.rxdroid.repository

import android.text.TextUtils
import com.rxdroid.api.error.RequestError
import com.rxdroid.api.github.provider.GitHubUserProvider
import com.rxdroid.repository.cache.UserCache
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.User
import fup.prototype.data.search.UserDatabaseProvider
import fup.prototype.data.search.UserDto
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserUiRepository @Inject constructor(
        val gitHubUserProvider: GitHubUserProvider,
        val userDatabaseProvider: UserDatabaseProvider) : UiRepository<User> {

    private val userCache: UserCache = UserCache(10L, TimeUnit.SECONDS)

    private var lastSearchValue: String? = null
    private var userResource: Resource<User>? = null

    override val cachedValue: Resource<User>?
        get() = userResource


    override fun loadBySearchValue(searchValue: String): Observable<Resource<User>> {
        if (TextUtils.isEmpty(searchValue)) {
            userResource = Resource.error(RequestError.create(RequestError.ERROR_CODE_NO_SEARCH_INPUT), null)
            return Observable.just(userResource)
        }
        if (hasValidCacheValue(searchValue)) {
            userResource = Resource.success(userResource?.data!!)
            return Observable.just(userResource)
        }
        this.lastSearchValue = searchValue
        return gitHubUserProvider.loadBySearchValue(searchValue)
                .map<Resource<User>> { userDataResponse ->
                    if (userDataResponse.isSuccessful) {
                        val user = User.fromApi(userDataResponse.body())
                        userCache.setData(user)
                        userResource = Resource.success(user)
                    } else {
                        val requestError = RequestError.create(userDataResponse, null)
                        userResource = Resource.error(requestError, null)
                    }
                    return@map userResource!!
                }
    }


    fun hasValidCacheValue(currentSearchValue: String): Boolean {
        return userResource != null && userResource?.data != null && TextUtils.equals(lastSearchValue, currentSearchValue) && userCache.hasValidCachedData()
    }

    fun updateDatabase(user: User): Completable {
        val userDto = UserDto()
        userDto.name = user.name
        userDto.login = user.login
        userDto.publicGistCount = user.publicGistCount
        userDto.publicRepoCount = user.publicRepoCount
        return userDatabaseProvider.insertOrUpdate(userDto)
    }

}