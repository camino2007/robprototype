package fup.prototype.robprototype.data.repositories;

import android.text.TextUtils;
import fup.prototype.data.RealmService;
import fup.prototype.data.RealmTable;
import fup.prototype.data.model.RealmUser;
import fup.prototype.domain.api.ApiCallAdapter;
import fup.prototype.domain.api.LoadingState;
import fup.prototype.domain.api.RequestError;
import fup.prototype.domain.github.model.GitHubUser;
import fup.prototype.domain.github.provider.GitHubProvider;
import fup.prototype.domain.github.provider.GitHubUserProvider;
import fup.prototype.robprototype.data.cache.UserCache;
import fup.prototype.robprototype.model.User;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import java.util.concurrent.TimeUnit;

public class UserRepository {

    private static final String TAG = "UserRepository";

    private final RealmService realmService;

    private final GitHubProvider gitHubProvider;
    private final GitHubUserProvider gitHubUserProvider;

    private UserCache userCache;
    private OnUserListener userListener;
    private String currentSearchValue;

    public UserRepository(@NonNull final GitHubProvider gitHubProvider,
                          @NonNull final GitHubUserProvider gitHubUserProvider,
                          @NonNull final RealmService realmService) {
        this.gitHubProvider = gitHubProvider;
        this.gitHubUserProvider = gitHubUserProvider;
        this.realmService = realmService;
        this.gitHubUserProvider.setApiCallListener(new GitHubUserListener());
        this.userCache = new UserCache(30, TimeUnit.SECONDS);
    }

    public void setUserListener(final OnUserListener userListener) {
        this.userListener = userListener;
    }

    public void load(@Nullable final String searchValue) {
        if (!TextUtils.isEmpty(searchValue)) {
            loadSingleUser(searchValue);
        } else {
            loadAllUser();
        }
    }

    private void loadAllUser() {

    }

    private void loadSingleUser(@NonNull  final String searchValue) {
        if (userCache.isSameUserCached(searchValue) && userCache.isCacheValid()) {
            if (userListener != null) {
                userListener.onUserLoaded(userCache.getData());
                return;
            }
        }
        currentSearchValue = searchValue;
        gitHubUserProvider.loadGitHubUser(searchValue);
    }

    private void handleErrorCase(@NonNull final RequestError requestError) {
        if (userCache.isSameUserCached(currentSearchValue) && userCache.isCacheValid()) {
            if (userListener != null) {
                userListener.onUserLoaded(userCache.getData());
            }
        } else {
            RealmUser realmUser = realmService.getRealm().where(RealmUser.class).equalTo(RealmTable.User.NAME, currentSearchValue).findFirst();

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

        void onUserLoaded(@Nullable final User user);

        void onError(@NonNull final RequestError requestError);

        void onLoadingStateChanged(@NonNull final LoadingState loadingState);
    }

    private class GitHubUserListener extends ApiCallAdapter<GitHubUser> {

        @Override
        public void onLoadingStateChanged(final LoadingState loadingState) {
            if (userListener != null) {
                userListener.onLoadingStateChanged(loadingState);
            }
        }

        @Override
        public void onApiCallDone(@Nullable final GitHubUser gitHubUser) {
            final RealmUser realmUser = RealmUser.fromDomainModel(gitHubUser);
            realmService.getRealm().beginTransaction();
            realmService.getRealm().copyToRealmOrUpdate(realmUser);
            realmService.getRealm().commitTransaction();
            final User user = User.fromRealm(realmUser);
            userCache.setData(user);
            if (userListener != null) {
                userListener.onUserLoaded(user);
            }
        }

        @Override
        public void onApiCallError(final RequestError requestError) {
            handleErrorCase(requestError);
        }
    }
}
