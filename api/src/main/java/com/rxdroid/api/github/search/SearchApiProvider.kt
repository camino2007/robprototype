package com.rxdroid.api.github.search

import com.rxdroid.api.github.model.GitHubUserData
import io.reactivex.Single
import retrofit2.Response

interface SearchApiProvider {

    fun findUserBySearchValue(searchValue: String): Single<Response<GitHubUserData>>

}