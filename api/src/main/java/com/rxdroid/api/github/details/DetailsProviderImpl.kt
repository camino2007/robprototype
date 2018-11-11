package com.rxdroid.api.github.details

import com.rxdroid.api.github.GitHubApi
import com.rxdroid.api.github.model.GitHubRepoData
import io.reactivex.Single
import retrofit2.Response

class DetailsProviderImpl(private val gitHubApi: GitHubApi) : DetailsApiProvider {

    override fun getRepositoriesForUser(user: String): Single<Response<List<GitHubRepoData>>> {
        return gitHubApi.getRepositoriesForUser(user)
    }

}