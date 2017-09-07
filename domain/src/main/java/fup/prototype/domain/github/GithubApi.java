package fup.prototype.domain.github;

import java.util.List;

import fup.prototype.domain.github.model.GitHubRepo;
import fup.prototype.domain.github.model.GitHubUser;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubApi {

    @GET("users/{user}/repos")
    Observable<Response<List<GitHubRepo>>> getReposForUser(@Path("user") String user);

    @GET("/users/{user}")
    Observable<Response<GitHubUser>> getUserByName(@Path("user") String user);

}
