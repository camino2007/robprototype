package fup.prototype.robprototype.data.repositories;

import android.support.annotation.NonNull;
import fup.prototype.data.UserRealmProvider;
import fup.prototype.domain.api.ApiCallAdapter;
import fup.prototype.domain.api.LoadingState;
import fup.prototype.domain.api.RequestError;
import fup.prototype.domain.github.model.GitHubRepo;
import fup.prototype.domain.github.provider.GitHubRepoProvider;
import fup.prototype.robprototype.view.main.model.Repository;
import io.reactivex.annotations.Nullable;
import java.util.List;

public class GitHubRepoRepository {

    private final GitHubRepoProvider gitHubRepoProvider;
    private final UserRealmProvider userRealmProvider;

    private GitHubUserListener gitHubUserListener;
    private OnRepoListener repoListener;

    public GitHubRepoRepository(@NonNull final GitHubRepoProvider gitHubRepoProvider, @NonNull final UserRealmProvider userRealmProvider) {
        this.gitHubRepoProvider = gitHubRepoProvider;
        this.gitHubRepoProvider.setApiCallListener(new GitHubUserListener());
        this.userRealmProvider = userRealmProvider;
    }

    public void setRepoListener(final OnRepoListener repoListener) {
        this.repoListener = repoListener;
    }

    public void load(@NonNull final String loginName) {
        gitHubRepoProvider.loadGithubRepos(loginName);
    }

    private void handleErrorCase(@NonNull final RequestError requestError) {
       /* if (userCache.isSameUserCached(currentSearchValue) && userCache.isCacheValid()) {
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
        }*/
    }

    public interface OnRepoListener {

        void onReposLoaded(@Nullable final List<Repository> repositories);

        void onError(@NonNull final RequestError requestError);

        void onLoadingStateChanged(@NonNull final LoadingState loadingState);
    }

    private class GitHubUserListener extends ApiCallAdapter<List<GitHubRepo>> {

        @Override
        public void onLoadingStateChanged(final LoadingState loadingState) {
            if (repoListener != null) {
                repoListener.onLoadingStateChanged(loadingState);
            }
        }

        @Override
        public void onApiCallDone(@Nullable final List<GitHubRepo> gitHubRepos) {
        /*    userRealmProvider.onStoreOrUpdate(gitHubUser);
            final User user = User.fromApiList(gitHubUser);
            userCache.setData(user);*/

            final List<Repository> repositories = Repository.fromApiList(gitHubRepos);

            if (repoListener != null) {
                repoListener.onReposLoaded(repositories);
            }
        }

        @Override
        public void onApiCallError(final RequestError requestError) {
            handleErrorCase(requestError);
        }
    }
}
