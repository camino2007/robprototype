package com.rxdroid.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rxdroid.api.RequestError;
import com.rxdroid.api.github.model.GitHubRepoModel;
import com.rxdroid.api.github.provider.GitHubRepositoryProvider;
import com.rxdroid.repository.model.Repository;
import com.rxdroid.repository.model.RepositoryResponse;

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
public class GithubDetailsUiRepository implements UiRepository<RepositoryResponse> {

    private RepositoryResponse repositoryResponse;
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
        return repositoryResponse != null && TextUtils.equals(lastSearchValue, currentSearchValue);
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
    public Observable<RepositoryResponse> loadBySearchValue(@NonNull final String searchValue) {

        if (hasValidCacheValue(searchValue)) {
            repositoryResponse = createRepositoryResponse(repositoryResponse.getRepositories(), null);
            return Observable.just(repositoryResponse);
        }
        lastSearchValue = searchValue;

        return gitHubRepositoryProvider.loadBySearchValue(searchValue)
                .map(new Function<Response<List<GitHubRepoModel>>, RepositoryResponse>() {
                    @Override
                    public RepositoryResponse apply(final Response<List<GitHubRepoModel>> listResponse) throws Exception {
                        if (listResponse != null && listResponse.isSuccessful()) {
                            final List<Repository> repositories = Repository.fromApiList(listResponse.body());
                            repositoryResponse = createRepositoryResponse(repositories, null);
                        } else {
                            repositoryResponse = createRepositoryResponse(null, RequestError.create(listResponse, null));
                        }
                        return repositoryResponse;
                    }
                });
    }

    private RepositoryResponse createRepositoryResponse(@Nullable final List<Repository> repositories, @Nullable final RequestError requestError) {
        return RepositoryResponse.create(repositories, requestError);
    }

    @Override
    public RepositoryResponse getCachedValue() {
        return repositoryResponse;
    }
}
