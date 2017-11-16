package fup.prototype.domain.github;

import fup.prototype.domain.github.model.GitHubRepo;
import fup.prototype.domain.github.model.GitHubUser;
import io.reactivex.Observable;
import java.util.List;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubApi {

    @GET("users/{user}/repos")
    Observable<Response<List<GitHubRepo>>> getReposForUser(@Path("user") String user);

    @GET("/users/{user}")
    Observable<Response<GitHubUser>> getUserByName(@Path("user") String user);

}
