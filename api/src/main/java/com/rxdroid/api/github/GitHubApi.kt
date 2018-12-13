package com.rxdroid.api.github

import com.rxdroid.api.github.model.GitHubRepoData
import com.rxdroid.api.github.model.GitHubUserData
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {

    @GET("users/{user}/repos")
    fun getRepositoriesForUser(@Path("user") user: String): Single<Response<List<GitHubRepoData>>>

    @GET("/users/{user}")
    fun getUserByLogin(@Path("user") user: String): Single<Response<GitHubUserData>>

}
