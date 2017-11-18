package fup.prototype.robprototype.view.details;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import fup.prototype.domain.api.LoadingState;
import fup.prototype.domain.api.RequestError;
import fup.prototype.robprototype.ProtoApplication;
import fup.prototype.robprototype.data.repositories.GitHubRepoRepository;
import fup.prototype.robprototype.view.base.viewmodels.BaseViewModel;
import fup.prototype.robprototype.view.main.model.Repository;
import fup.prototype.robprototype.view.main.model.User;
import java.util.List;
import javax.inject.Inject;

public class DetailViewModel extends BaseViewModel {

    public ObservableField<String> userName = new ObservableField<>();
    public ObservableArrayList<Repository> items = new ObservableArrayList<>();
    private User user;

    @Inject
    GitHubRepoRepository gitHubRepoRepository;

    public DetailViewModel() {
        gitHubRepoRepository.setRepoListener(new RepoListener());
    }

    @Override
    protected void injectDependencies() {
        ProtoApplication.getAppComponent().inject(this);
    }

    @Override
    public void loadOrShowData() {
        gitHubRepoRepository.load(user.getLogin());
    }

    public void setUser(final User user) {
        this.user = user;
    }

    private class RepoListener implements GitHubRepoRepository.OnRepoListener {

        @Override
        public void onReposLoaded(final List<Repository> repositories) {
            items.clear();
            items.addAll(repositories);
        }

        @Override
        public void onError(@NonNull final RequestError requestError) {
            onDataError(requestError);
        }

        @Override
        public void onLoadingStateChanged(@NonNull final LoadingState loadingState) {
            changeLoadingState(loadingState == LoadingState.LOADING);
        }
    }
}
