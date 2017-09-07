package fup.prototype.domain.github.provider;


import fup.prototype.domain.api.ApiProvider;
import fup.prototype.domain.github.GithubApi;
import fup.prototype.domain.github.model.GitHubUser;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import retrofit2.Response;

public class GitHubUserProvider extends ApiProvider<GitHubUser> {

    public GitHubUserProvider(@NonNull GithubApi githubApi) {
        super(githubApi);
    }

    public void loadGithubUserRepos(final String userName) {
        final Observable<Response<GitHubUser>> observable = getGithubApi().getUserByName(userName);
        execute(observable);
    }
}
