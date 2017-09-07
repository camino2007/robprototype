package fup.prototype.domain.github.provider;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fup.prototype.domain.api.ApiProvider;
import fup.prototype.domain.github.GithubApi;
import fup.prototype.domain.github.model.GitHubRepo;
import fup.prototype.domain.github.model.GitHubUser;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import retrofit2.Response;

public class GitHubProvider extends ApiProvider<Map<GitHubUser, List<GitHubRepo>>> {

    public GitHubProvider(@NonNull GithubApi githubApi) {
        super(githubApi);
    }

    public void loadGitHubData(final String userName) {
        final Observable<Map<GitHubUser, List<GitHubRepo>>> observable =
                Observable.zip(
                        getGithubApi().getUserByName(userName),
                        getGithubApi().getReposForUser(userName),
                        new BiFunction<Response<GitHubUser>, Response<List<GitHubRepo>>, Map<GitHubUser, List<GitHubRepo>>>() {
                            @Override
                            public Map<GitHubUser, List<GitHubRepo>> apply(@NonNull Response<GitHubUser> gitHubUserResponse, @NonNull Response<List<GitHubRepo>> listResponse) throws Exception {
                                final Map<GitHubUser, List<GitHubRepo>> userListMap = new LinkedHashMap<>();
                                userListMap.put(gitHubUserResponse.body(), listResponse.body());
                                return userListMap;
                            }
                        }
                );
        executeZip(observable);
    }
}
