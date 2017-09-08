package fup.prototype.robprototype.model;

import android.util.Log;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fup.prototype.data.RealmService;
import fup.prototype.data.model.RealmUser;
import fup.prototype.domain.api.ApiCallAdapter;
import fup.prototype.domain.api.LoadingState;
import fup.prototype.domain.api.RequestError;
import fup.prototype.domain.github.model.GitHubRepo;
import fup.prototype.domain.github.model.GitHubUser;
import fup.prototype.domain.github.provider.GitHubProvider;
import fup.prototype.robprototype.data.cache.UserCache;
import io.reactivex.annotations.NonNull;

public class UserRepository {

    private static final String TAG = "UserRepository";

    private final RealmService realmService;

    private final GitHubProvider gitHubProvider;

    private UserCache userCache;
    private OnUserListener userListener;

    public UserRepository(@NonNull final GitHubProvider gitHubProvider, @NonNull final RealmService realmService) {
        this.gitHubProvider = gitHubProvider;
        this.realmService = realmService;
        this.userCache = new UserCache(30, TimeUnit.SECONDS);
    }

    public void setUserListener(final OnUserListener userListener) {
        this.userListener = userListener;
    }

   /* public void load(final String userName) {

        RealmUser realmUser = realmService.getRealm()
                .where(RealmUser.class)
                .equalTo(RealmTable.User.NAME, userName)
                .findFirst();

        if (realmUser == null) {




            gitHubProvider.loadGitHubData(userName);
        } else {
            Log.d(TAG, "load: User found!");
            if (userListener != null) {
                final User user = User.fromRealm(realmUser);
                userListener.onUserLoaded(user);
                userListener.onLoadingStateChanged(LoadingState.DONE);
            }
        }
    }*/

    public void load(final String userName) {
        Log.d(TAG, "load: " + userName);
        //TODO Check if we have a different user
        if (userCache.hasCachedData()
                && userCache.isCacheValid()) {
            if (userListener != null) {
                userListener.onUserLoaded(userCache.getData());
                return;
            }
        }
        gitHubProvider.setApiCallListener(new GitHubListener());
        gitHubProvider.loadGitHubData(userName);
    }

    public interface OnUserListener {

        void onUserLoaded(@NonNull final User user);

        void onError(@NonNull RequestError requestError);

        void onLoadingStateChanged(@NonNull LoadingState loadingState);
    }

    private class GitHubListener extends ApiCallAdapter<Map<GitHubUser, List<GitHubRepo>>> {
        @Override
        public void onApiCallError(@NonNull RequestError requestError) {
            Log.d(TAG, "onApiCallError: ");
            if (userListener != null) {
                userListener.onError(requestError);
            }
        }

        @Override
        public void onLoadingStateChanged(@NonNull LoadingState loadingState) {
            Log.d(TAG, "onLoadingStateChanged: " + loadingState);
            if (userListener != null) {
                userListener.onLoadingStateChanged(loadingState);
            }
        }

        @Override
        public void onApiCallDone(@NonNull Map<GitHubUser, List<GitHubRepo>> gitHubUserListMap) {
            if (!gitHubUserListMap.isEmpty()) {
                for (Map.Entry<GitHubUser, List<GitHubRepo>> gitHubUserListEntry : gitHubUserListMap.entrySet()) {
                    GitHubUser gitHubUser = gitHubUserListEntry.getKey();
                    List<GitHubRepo> gitHubRepos = gitHubUserListEntry.getValue();
                    RealmUser realmUser = RealmUser.fromDomainModel(gitHubUser, gitHubRepos);
//                        realmService.getRealm().beginTransaction();

                      /*  realmService.getRealm().copyToRealm(realmUser);
                        realmService.getRealm().commitTransaction();*/
                    final User user = User.fromRealm(realmUser);

                    userCache.setData(user);
                    if (userListener != null) {
                        userListener.onUserLoaded(user);
                    }
                }
            }
        }
    }
}
