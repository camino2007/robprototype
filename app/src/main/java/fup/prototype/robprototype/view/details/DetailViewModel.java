package fup.prototype.robprototype.view.details;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import com.rxdroid.repository.model.Repository;
import com.rxdroid.repository.model.User;
import fup.prototype.robprototype.ProtoApplication;
import fup.prototype.robprototype.view.base.viewmodels.BaseViewModel;

public class DetailViewModel extends BaseViewModel {

    public ObservableField<String> userName = new ObservableField<>();
    public ObservableArrayList<Repository> items = new ObservableArrayList<>();
    private User user;

  /*  @Inject
    GitHubRepoRepository gitHubRepoRepository;*/

    public DetailViewModel() {
        //    gitHubRepoRepository.setRepoListener(new RepoListener());
    }

    @Override
    protected void injectDependencies() {
        ProtoApplication.getAppComponent().inject(this);
    }

    @Override
    public void loadOrShowData() {
        //gitHubRepoRepository.load(user.getLogin());
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

   /* private class RepoListener implements GitHubRepoRepository.OnRepoListener {

        @Override
        public void onReposLoaded(final List<Repository> repositories) {
            items.clear();
            items.addAll(repositories);
        }

        @Override
        public void onError(@NonNull final RequestError requestError) {
            handleErrorCase(requestError);
        }

        @Override
        public void onDatabaseStateChanged(@NonNull final LoadingState loadingState) {
            changeLoadingState(loadingState == LoadingState.LOADING);
        }
    }*/
}
