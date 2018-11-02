package com.rxdroid.api.github.search

import com.rxdroid.api.github.GitHubApi
import com.rxdroid.api.github.model.GitHubUserData
import io.reactivex.Single
import retrofit2.Response

class SearchProviderImpl(private val gitHubApi: GitHubApi) : SearchApiProvider {

    override fun findUserBySearchValue(searchValue: String): Single<Response<GitHubUserData>> {
        return gitHubApi.getUserByName(searchValue)
    }

}