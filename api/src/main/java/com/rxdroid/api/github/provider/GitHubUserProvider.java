package com.rxdroid.api.github.provider;

import android.support.annotation.NonNull;
import com.rxdroid.api.github.GitHubApi;
import com.rxdroid.api.github.model.GitHubUserModel;
import dagger.Reusable;
import io.reactivex.Observable;
import javax.inject.Inject;
import retrofit2.Response;

@Reusable
public class GitHubUserProvider extends GitHubApiProvider implements ApiProvider<GitHubUserModel> {

    @Inject
    public GitHubUserProvider(final GitHubApi gitHubApi) {
        super(gitHubApi);
    }

    @Override
    public Observable<Response<GitHubUserModel>> loadBySearchValue(@NonNull final String searchValue) {
        return getGitHubApi().getUserByName(searchValue);
    }
}
