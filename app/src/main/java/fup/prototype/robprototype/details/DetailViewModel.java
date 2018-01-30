package fup.prototype.robprototype.details;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.rxdroid.api.RequestError;
import com.rxdroid.repository.GithubDetailsUiRepository;
import com.rxdroid.repository.model.Repository;
import com.rxdroid.repository.model.RepositoryResponse;
import com.rxdroid.repository.model.User;

import java.util.List;

import javax.inject.Inject;

import dagger.Reusable;
import fup.prototype.robprototype.view.base.adapters.ObserverAdapter;
import fup.prototype.robprototype.view.base.viewmodels.BaseViewModel;
import fup.prototype.robprototype.view.base.viewmodels.ViewState;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Reusable
public class DetailViewModel extends BaseViewModel {

    private static final String TAG = "DetailViewModel";

    public ObservableField<String> userName = new ObservableField<>();
    public ObservableArrayList<Repository> items = new ObservableArrayList<>();

    private User user;

    @NonNull
    private final GithubDetailsUiRepository detailsUiRepository;

    @Inject
    public DetailViewModel(@NonNull final GithubDetailsUiRepository detailsUiRepository) {
        this.detailsUiRepository = detailsUiRepository;
    }

    @Override
    public void loadOrShowData() {
        if (user != null) {
            if (detailsUiRepository.hasValidCacheValue(user.getLogin())) {
                if (detailsUiRepository.getCachedValue().getRequestError() != null) {
                    items.clear();
                    handleErrorCase(detailsUiRepository.getCachedValue().getRequestError());
                } else {
                    handleSuccessCase(detailsUiRepository.getCachedValue().getRepositories());
                }
            } else {
                detailsUiRepository.loadBySearchValue(user.getLogin())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RepositoryObserver());
            }
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    private void handleSuccessCase(final List<Repository> repositories) {
        if (!repositories.isEmpty()) {
            setViewState(ViewState.ON_LOADED);
            showRepositories(repositories);
        } else {
            setViewState(ViewState.ON_NO_DATA);
        }
    }

    private void showRepositories(final List<Repository> repositories) {
        items.clear();
        items.addAll(repositories);
    }

    private class RepositoryObserver extends ObserverAdapter<RepositoryResponse> {

        @Override
        public void onSubscribe(final Disposable d) {
            getCompositeDisposable().add(d);
        }

        @Override
        public void onNext(final RepositoryResponse repositoryResponse) {
            changeLoadingState(false);
            if (repositoryResponse.getRequestError() != null) {
                items.clear();
                handleErrorCase(repositoryResponse.getRequestError());
            } else {
                handleSuccessCase(repositoryResponse.getRepositories());
            }
        }

        @Override
        public void onError(final Throwable e) {
            Log.e(TAG, "onError: ", e);
            handleErrorCase(RequestError.create(null, e));
        }

    }


}
