package fup.prototype.domain.github.provider;

import fup.prototype.domain.api.ApiProvider;
import fup.prototype.domain.github.GitHubApi;
import fup.prototype.domain.github.model.GitHubRepo;
import fup.prototype.domain.github.model.GitHubUser;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Response;

public class GitHubProvider extends ApiProvider<Map<GitHubUser, List<GitHubRepo>>> {

    public GitHubProvider(@NonNull GitHubApi gitHubApi) {
        super(gitHubApi);
    }

    public void loadGitHubData(final String userName) {
        final Observable<Map<GitHubUser, List<GitHubRepo>>> observable =
                Observable.zip(getGitHubApi().getUserByName(userName), getGitHubApi().getReposForUser(userName),
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
