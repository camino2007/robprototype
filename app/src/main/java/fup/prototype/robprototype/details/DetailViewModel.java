package fup.prototype.robprototype.details;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.util.Log;
import com.rxdroid.api.RequestError;
import com.rxdroid.repository.GithubDetailsUiRepository;
import com.rxdroid.repository.model.Repository;
import com.rxdroid.repository.model.RepositoryResponse;
import com.rxdroid.repository.model.User;
import fup.prototype.robprototype.ProtoApplication;
import fup.prototype.robprototype.view.base.viewmodels.BaseViewModel;
import fup.prototype.robprototype.view.base.viewmodels.ViewState;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;

public class DetailViewModel extends BaseViewModel {

    private static final String TAG = "DetailViewModel";

    public ObservableField<String> userName = new ObservableField<>();
    public ObservableArrayList<Repository> items = new ObservableArrayList<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private User user;

    @Inject
    GithubDetailsUiRepository detailsUiRepository;

    @Override
    protected void injectDependencies() {
        ProtoApplication.getAppComponent().inject(this);
    }

    @Override
    public void loadOrShowData() {
        Log.d(TAG, "loadOrShowData: ");
        if (user != null) {
            detailsUiRepository.loadBySearchValue(user.getLogin())
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(new RepositoryObserver());
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        Log.d(TAG, "setUser: ");
        this.user = user;
    }

    private class RepositoryObserver implements Observer<RepositoryResponse> {

        @Override
        public void onSubscribe(final Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(final RepositoryResponse repositoryResponse) {
            Log.d(TAG, "onNext: ");
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

        @Override
        public void onComplete() {
        }
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
}
