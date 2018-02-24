package com.rxdroid.api.github

import com.rxdroid.api.github.model.GitHubRepoData
import com.rxdroid.api.github.model.GitHubUserData
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {

    @GET("users/{user}/repos")
    fun getReposForUser(@Path("user") user: String): Observable<Response<List<GitHubRepoData>>>

    @GET("/users/{user}")
    fun getUserByName(@Path("user") user: String): Observable<Response<GitHubUserData>>

}
