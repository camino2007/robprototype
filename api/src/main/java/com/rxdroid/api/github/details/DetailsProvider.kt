package com.rxdroid.api.github.details

import com.rxdroid.api.github.model.GitHubRepoData
import io.reactivex.Single
import retrofit2.Response

interface DetailsProvider {

    fun getRepositoriesForUser(user: String): Single<Response<List<GitHubRepoData>>>

}