package fup.prototype.domain.github.provider;


import fup.prototype.domain.api.ApiProvider;
import fup.prototype.domain.github.GitHubApi;
import fup.prototype.domain.github.model.GitHubUser;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import retrofit2.Response;

public class GitHubUserProvider extends ApiProvider<GitHubUser> {

    public GitHubUserProvider(@NonNull GitHubApi gitHubApi) {
        super(gitHubApi);
    }

    public void loadGitHubUser(final String userName) {
        final Observable<Response<GitHubUser>> observable = getGitHubApi().getUserByName(userName);
        execute(observable);
    }
}
