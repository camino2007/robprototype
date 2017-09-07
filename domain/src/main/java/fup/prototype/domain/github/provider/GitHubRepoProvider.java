package fup.prototype.domain.github.provider;

import java.util.List;

import fup.prototype.domain.api.ApiProvider;
import fup.prototype.domain.github.GithubApi;
import fup.prototype.domain.github.model.GitHubRepo;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import retrofit2.Response;

public class GitHubRepoProvider extends ApiProvider<List<GitHubRepo>> {

    public GitHubRepoProvider(@NonNull final GithubApi githubApi) {
        super(githubApi);
    }

    public void loadGithubRepos(final String userName) {
        final Observable<Response<List<GitHubRepo>>> observable = getGithubApi().getReposForUser(userName);
        execute(observable);
    }
}
