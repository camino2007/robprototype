package fup.prototype.robprototype.data.repositories;

import android.text.TextUtils;
import fup.prototype.data.UserRealmProvider;
import fup.prototype.data.models.main.UserEntity;
import fup.prototype.domain.api.ApiCallAdapter;
import fup.prototype.domain.api.LoadingState;
import fup.prototype.domain.api.RequestError;
import fup.prototype.domain.github.model.GitHubUser;
import fup.prototype.domain.github.provider.GitHubUserProvider;
import fup.prototype.robprototype.data.cache.UserCache;
import fup.prototype.robprototype.view.main.model.User;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import java.util.concurrent.TimeUnit;

public class UserRepository {

    private static final String TAG = "UserRepository";

    private final GitHubUserProvider gitHubUserProvider;
    private final UserRealmProvider userRealmProvider;

    private UserCache userCache;
    private OnUserListener userListener;
    private String currentSearchValue;

    public UserRepository(@NonNull final GitHubUserProvider gitHubUserProvider, @NonNull final UserRealmProvider userRealmProvider) {
        this.gitHubUserProvider = gitHubUserProvider;
        this.userRealmProvider = userRealmProvider;
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
        //TODO
    }

    private void loadSingleUser(@NonNull final String searchValue) {
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
            final UserEntity userEntity = userRealmProvider.getForStringValue(currentSearchValue);

            if (userEntity == null) {
                if (userListener != null) {
                    userListener.onError(requestError);
                }
            } else {
                final User user = User.fromEntity(userEntity);
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
            userRealmProvider.onStoreOrUpdate(gitHubUser);
            final User user = User.fromApi(gitHubUser);
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
