package com.rxdroid.api.github.provider

import com.rxdroid.api.github.GitHubApi
import com.rxdroid.api.github.model.GitHubUserData
import dagger.Reusable
import io.reactivex.Observable
import retrofit2.Response
import javax.inject.Inject

@Reusable
class GitHubUserProvider @Inject
constructor(gitHubApi: GitHubApi) : GitHubApiProvider(gitHubApi), ApiProvider<GitHubUserData> {

    override fun loadBySearchValue(searchValue: String): Observable<Response<GitHubUserData>> {
        return gitHubApi.getUserByName(searchValue)
    }

}
