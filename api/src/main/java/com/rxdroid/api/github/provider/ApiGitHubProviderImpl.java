package com.rxdroid.api.github.provider;

import android.support.annotation.NonNull;
import com.rxdroid.api.ApiProvider;
import com.rxdroid.api.github.GitHubApi;
import com.rxdroid.api.github.model.GitHubRepoModel;
import com.rxdroid.api.github.model.GitHubUserModel;
import dagger.Reusable;
import io.reactivex.Observable;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Response;

@Reusable
public class ApiGitHubProviderImpl extends ApiProvider<Response<GitHubUserModel>> implements ApiGitHubProvider {

    @Inject
    public ApiGitHubProviderImpl(@NonNull final GitHubApi gitHubApi) {
        super(gitHubApi);
    }

    @Override
    public Observable<Response<GitHubUserModel>> loadGitHubUser(@NonNull final String userName) {
        return getGitHubApi().getUserByName(userName);
    }

    @Override
    public Observable<Response<List<GitHubRepoModel>>> loadGitHubRepositories(@NonNull final String userLogin) {
        return getGitHubApi().getReposForUser(userLogin);
    }
}
