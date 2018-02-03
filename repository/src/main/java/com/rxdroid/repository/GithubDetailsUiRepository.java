package com.rxdroid.repository;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.rxdroid.api.error.RequestError;
import com.rxdroid.api.github.model.GitHubRepoModel;
import com.rxdroid.api.github.provider.GitHubRepositoryProvider;
import com.rxdroid.repository.model.Repository;
import com.rxdroid.repository.model.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import fup.prototype.data.details.RepositoryDatabaseProvider;
import fup.prototype.data.details.RepositoryDto;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Response;

@Singleton
public class GithubDetailsUiRepository implements UiRepository<List<Repository>> {

    private Resource<List<Repository>> listResource;
    private String lastSearchValue;

    @NonNull
    private final GitHubRepositoryProvider gitHubRepositoryProvider;

    @NonNull
    private final RepositoryDatabaseProvider repositoryDatabaseProvider;

    @Inject
    public GithubDetailsUiRepository(@NonNull final GitHubRepositoryProvider gitHubRepositoryProvider,
                                     @NonNull final RepositoryDatabaseProvider repositoryDatabaseProvider) {
        this.gitHubRepositoryProvider = gitHubRepositoryProvider;
        this.repositoryDatabaseProvider = repositoryDatabaseProvider;
    }

    public boolean hasValidCacheValue(@NonNull final String currentSearchValue) {
        return listResource != null && TextUtils.equals(lastSearchValue, currentSearchValue);
    }

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

    @Override
    public Observable<Resource<List<Repository>>> loadBySearchValue(@NonNull final String searchValue) {
        if (hasValidCacheValue(searchValue) && listResource.data != null) {
            listResource = Resource.success(listResource.data);
            return Observable.just(listResource);
        }
        lastSearchValue = searchValue;

        return gitHubRepositoryProvider.loadBySearchValue(searchValue)
                .map(new Function<Response<List<GitHubRepoModel>>, Resource<List<Repository>>>() {
                    @Override
                    public Resource<List<Repository>> apply(final Response<List<GitHubRepoModel>> listResponse) {
                        if (listResponse != null && listResponse.isSuccessful()) {
                            final List<Repository> repositories = Repository.fromApiList(listResponse.body());
                            listResource = Resource.success(repositories);
                        } else {
                            final RequestError requestError = RequestError.create(listResponse, null);
                            listResource = Resource.error(requestError, null);
                        }
                        return listResource;
                    }
                });
    }

    @Override
    public Resource<List<Repository>> getCachedValue() {
        return listResource;
    }

}
