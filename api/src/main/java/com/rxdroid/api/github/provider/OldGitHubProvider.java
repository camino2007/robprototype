package com.rxdroid.api.github.provider;

import com.rxdroid.api.ApiProviderOld;
import com.rxdroid.api.github.GitHubApi;
import com.rxdroid.api.github.model.GitHubRepoModel;
import com.rxdroid.api.github.model.GitHubUserModel;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Response;

public class OldGitHubProvider extends ApiProviderOld<Map<GitHubUserModel, List<GitHubRepoModel>>> {

    public OldGitHubProvider(@NonNull GitHubApi gitHubApi) {
        super(gitHubApi);
    }

    public void loadGitHubData(final String userName) {
        final Observable<Map<GitHubUserModel, List<GitHubRepoModel>>> observable = Observable.zip(getGitHubApi().getUserByName(userName),
                                                                                                  getGitHubApi().getReposForUser(userName),
                                                                                                  new BiFunction<Response<GitHubUserModel>, Response<List<GitHubRepoModel>>, Map<GitHubUserModel, List<GitHubRepoModel>>>() {
                                                                                                      @Override
                                                                                                      public Map<GitHubUserModel, List<GitHubRepoModel>> apply(
                                                                                                              @NonNull
                                                                                                                      Response<GitHubUserModel> gitHubUserResponse,
                                                                                                              @NonNull
                                                                                                                      Response<List<GitHubRepoModel>> listResponse) throws
                                                                                                              Exception {
                                                                                                          final Map<GitHubUserModel, List<GitHubRepoModel>> userListMap = new LinkedHashMap<>();
                                                                                                          userListMap.put(gitHubUserResponse.body(),
                                                                                                                          listResponse.body());
                                                                                                          return userListMap;
                                                                                                      }
                                                                                                  });
        //execute(observable);
    }
}
