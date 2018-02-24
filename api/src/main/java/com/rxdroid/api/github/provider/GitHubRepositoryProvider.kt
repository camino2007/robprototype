package com.rxdroid.api.github.provider

import com.rxdroid.api.github.GitHubApi
import com.rxdroid.api.github.model.GitHubRepoData
import dagger.Reusable
import io.reactivex.Observable
import retrofit2.Response
import javax.inject.Inject

@Reusable
class GitHubRepositoryProvider @Inject
constructor(gitHubApi: GitHubApi) : GitHubApiProvider(gitHubApi), ApiProvider<List<GitHubRepoData>> {

    override fun loadBySearchValue(searchValue: String): Observable<Response<List<GitHubRepoData>>> {
        return gitHubApi.getReposForUser(searchValue)
    }
}
