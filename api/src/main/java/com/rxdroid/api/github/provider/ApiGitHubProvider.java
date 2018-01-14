package com.rxdroid.api.github.provider;

import android.support.annotation.NonNull;
import com.rxdroid.api.github.model.GitHubRepoModel;
import com.rxdroid.api.github.model.GitHubUserModel;
import io.reactivex.Observable;
import java.util.List;
import retrofit2.Response;

public interface ApiGitHubProvider {

    Observable<Response<GitHubUserModel>> loadGitHubUser(@NonNull final String userName);

    Observable<Response<List<GitHubRepoModel>>> loadGitHubRepositories(@NonNull final String userLogin);
}
