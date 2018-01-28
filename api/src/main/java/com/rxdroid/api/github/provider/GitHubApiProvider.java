package com.rxdroid.api.github.provider;

import com.rxdroid.api.github.GitHubApi;
import io.reactivex.annotations.NonNull;

public abstract class GitHubApiProvider {

    @NonNull
    private final GitHubApi gitHubApi;

    protected GitHubApiProvider(final GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    @NonNull
    public GitHubApi getGitHubApi() {
        return gitHubApi;
    }
}
