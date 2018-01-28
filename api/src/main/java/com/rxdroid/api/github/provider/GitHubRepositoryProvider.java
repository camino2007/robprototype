package com.rxdroid.api.github.provider;

import android.support.annotation.NonNull;
import com.rxdroid.api.github.GitHubApi;
import com.rxdroid.api.github.model.GitHubRepoModel;
import dagger.Reusable;
import io.reactivex.Observable;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Response;

@Reusable
public class GitHubRepositoryProvider extends GitHubApiProvider implements ApiProvider<List<GitHubRepoModel>> {

    @Inject
    public GitHubRepositoryProvider(final GitHubApi gitHubApi) {
        super(gitHubApi);
    }

    @Override
    public Observable<Response<List<GitHubRepoModel>>> loadBySearchValue(@NonNull final String searchValue) {
        return getGitHubApi().getReposForUser(searchValue);
    }
}
