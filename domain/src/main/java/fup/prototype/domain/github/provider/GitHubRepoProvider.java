package fup.prototype.domain.github.provider;

import fup.prototype.domain.api.ApiProvider;
import fup.prototype.domain.github.GitHubApi;
import fup.prototype.domain.github.model.GitHubRepo;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import java.util.List;
import retrofit2.Response;

public class GitHubRepoProvider extends ApiProvider<List<GitHubRepo>> {

    public GitHubRepoProvider(@NonNull final GitHubApi gitHubApi) {
        super(gitHubApi);
    }

    public void loadGithubRepos(final String userLogin) {
        final Observable<Response<List<GitHubRepo>>> observable = getGitHubApi().getReposForUser(userLogin);
        execute(observable);
    }
}
