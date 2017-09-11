package fup.prototype.robprototype.data.repositories;

import android.util.Log;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fup.prototype.data.RealmService;
import fup.prototype.data.RealmTable;
import fup.prototype.data.model.RealmUser;
import fup.prototype.domain.api.ApiCallAdapter;
import fup.prototype.domain.api.LoadingState;
import fup.prototype.domain.api.RequestError;
import fup.prototype.domain.github.model.GitHubRepo;
import fup.prototype.domain.github.model.GitHubUser;
import fup.prototype.domain.github.provider.GitHubProvider;
import fup.prototype.robprototype.data.cache.UserCache;
import fup.prototype.robprototype.model.User;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class UserRepository {

    private static final String TAG = "UserRepository";

    private final RealmService realmService;

    private final GitHubProvider gitHubProvider;

    private UserCache userCache;
    private OnUserListener userListener;
    private String currentSearchValue;

    public UserRepository(@NonNull final GitHubProvider gitHubProvider, @NonNull final RealmService realmService) {
        this.gitHubProvider = gitHubProvider;
        this.realmService = realmService;
        this.gitHubProvider.setApiCallListener(new GitHubListener());
        this.userCache = new UserCache(30, TimeUnit.SECONDS);
    }

    public void setUserListener(final OnUserListener userListener) {
        this.userListener = userListener;
    }

    public void load(@NonNull final String userName) {
        Log.d(TAG, "load: " + userName);
        if (userCache.isSameUserCached(userName)
                && userCache.isCacheValid()) {
            Log.d(TAG, "show cached data");
            if (userListener != null) {
                userListener.onUserLoaded(userCache.getData());
                return;
            }
        }
        currentSearchValue = userName;
        gitHubProvider.loadGitHubData(userName);
    }


    private void handleErrorCase(@NonNull final RequestError requestError) {
        if (userCache.isSameUserCached(currentSearchValue)
                && userCache.isCacheValid()) {
            if (userListener != null) {
                userListener.onUserLoaded(userCache.getData());
            }
        } else {
            RealmUser realmUser = realmService.getRealm()
                    .where(RealmUser.class)
                    .equalTo(RealmTable.User.NAME, currentSearchValue)
                    .findFirst();

            if (realmUser == null) {
                if (userListener != null) {
                    userListener.onError(requestError);
                }
            } else {
                final User user = User.fromRealm(realmUser);
                if (userListener != null) {
                    userListener.onUserLoaded(user);
                }
            }
        }
    }

    public interface OnUserListener {

        void onUserLoaded(@NonNull final User user);

        void onError(@NonNull RequestError requestError);

        void onLoadingStateChanged(@NonNull LoadingState loadingState);
    }

    private class GitHubListener extends ApiCallAdapter<Map<GitHubUser, List<GitHubRepo>>> {
        @Override
        public void onApiCallError(@NonNull RequestError requestError) {
            Log.d(TAG, "onApiCallError");
            handleErrorCase(requestError);
        }

        @Override
        public void onLoadingStateChanged(@NonNull final LoadingState loadingState) {
            if (userListener != null) {
                userListener.onLoadingStateChanged(loadingState);
            }
        }

        @Override
        public void onApiCallDone(@Nullable final Map<GitHubUser, List<GitHubRepo>> gitHubUserListMap) {
            Log.d(TAG, "onApiCallDone");
            if (gitHubUserListMap != null && !gitHubUserListMap.isEmpty()) {
                for (Map.Entry<GitHubUser, List<GitHubRepo>> gitHubUserListEntry : gitHubUserListMap.entrySet()) {
                    final GitHubUser gitHubUser = gitHubUserListEntry.getKey();
                    final List<GitHubRepo> gitHubRepos = gitHubUserListEntry.getValue();
                    final RealmUser realmUser = RealmUser.fromDomainModel(gitHubUser, gitHubRepos);
                    realmService.getRealm().beginTransaction();
                    realmService.getRealm().copyToRealmOrUpdate(realmUser);
                    realmService.getRealm().commitTransaction();
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
