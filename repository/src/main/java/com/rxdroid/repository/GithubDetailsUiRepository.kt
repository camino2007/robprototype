package com.rxdroid.repository

import android.text.TextUtils
import com.rxdroid.api.error.RequestError
import com.rxdroid.api.github.provider.GitHubRepositoryProvider
import com.rxdroid.repository.model.Repository
import com.rxdroid.repository.model.Resource
import fup.prototype.data.details.RepositoryDatabaseProvider
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubDetailsUiRepository @Inject constructor(
        val gitHubRepositoryProvider: GitHubRepositoryProvider,
        val repositoryDatabaseProvider: RepositoryDatabaseProvider) : UiRepository<List<Repository>> {


    private var listResource: Resource<List<Repository>>? = null
    private var lastSearchValue: String? = null

    override val cachedValue: Resource<List<Repository>>?
        get() = listResource

    override fun loadBySearchValue(searchValue: String): Observable<Resource<List<Repository>>> {
        if (hasValidCacheValue(searchValue)) {
            listResource = Resource.success(listResource?.data!!)
            return Observable.just(listResource)
        }
        lastSearchValue = searchValue
        return gitHubRepositoryProvider.loadBySearchValue(searchValue)
                .map<Resource<List<Repository>>> { listResponse ->
                    if (listResponse.isSuccessful) {
                        val repositories = Repository.fromApiList(listResponse.body())
                        listResource = Resource.success(repositories)
                    } else {
                        val requestError = RequestError.create(listResponse, null)
                        listResource = Resource.error(requestError, null)
                    }
                    return@map listResource!!
                }
    }


/*

    public Completable updateDatabase(@NonNull final List<Repository> repositories, final int githubUserId) {
        final List<RepositoryDto> repositoryDtos = new ArrayList<>();
        for (Repository repository : repositories) {
            RepositoryDto dto = new RepositoryDto();
            dto.name = repository.getName();
            dto.githubUserId = githubUserId;
            dto.fullName = repository.getFullName();
            dto.idRep = repository.getId();
            repositoryDtos.add(dto);
        }
        return repositoryDatabaseProvider.insertOrUpdate(repositoryDtos);
    }

*/

    fun hasValidCacheValue(currentSearchValue: String): Boolean {
        return listResource != null && listResource?.data != null && TextUtils.equals(lastSearchValue, currentSearchValue)
    }
}