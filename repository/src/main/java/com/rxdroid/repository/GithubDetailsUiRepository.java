package com.rxdroid.repository;

import android.support.annotation.NonNull;
import com.rxdroid.api.RequestError;
import com.rxdroid.api.github.model.GitHubRepoModel;
import com.rxdroid.api.github.provider.GitHubRepositoryProvider;
import com.rxdroid.repository.model.Repository;
import com.rxdroid.repository.model.RepositoryResponse;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Response;

@Singleton
public class GithubDetailsUiRepository implements UiRepository<RepositoryResponse> {

    private final GitHubRepositoryProvider gitHubRepositoryProvider;

    @Inject
    public GithubDetailsUiRepository(final GitHubRepositoryProvider gitHubRepositoryProvider) {
        this.gitHubRepositoryProvider = gitHubRepositoryProvider;
    }

    @Override
    public Observable<RepositoryResponse> loadBySearchValue(@NonNull final String searchValue) {
        return gitHubRepositoryProvider.loadBySearchValue(searchValue).map(new Function<Response<List<GitHubRepoModel>>, RepositoryResponse>() {
            @Override
            public RepositoryResponse apply(final Response<List<GitHubRepoModel>> listResponse) throws Exception {
                if (listResponse != null && listResponse.isSuccessful()) {
                    final List<Repository> repositories = Repository.fromApiList(listResponse.body());
                    return RepositoryResponse.create(repositories, null);
                } else {
                    return RepositoryResponse.create(null, RequestError.create(listResponse, null));
                }
            }
        });
    }

    @Override
    public boolean hasCachedValue() {
        return false;
    }
}
