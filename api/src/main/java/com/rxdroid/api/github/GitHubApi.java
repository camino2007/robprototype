package com.rxdroid.api.github;

import com.rxdroid.api.github.model.GitHubRepoModel;
import com.rxdroid.api.github.model.GitHubUserModel;
import io.reactivex.Observable;
import java.util.List;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubApi {

    @GET("users/{user}/repos")
    Observable<Response<List<GitHubRepoModel>>> getReposForUser(@Path("user") String user);

    @GET("/users/{user}")
    Observable<Response<GitHubUserModel>> getUserByName(@Path("user") String user);

}
