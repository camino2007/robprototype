package fup.prototype.robprototype.details;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.rxdroid.api.error.RequestError;
import com.rxdroid.repository.GithubDetailsUiRepository;
import com.rxdroid.repository.model.Repository;
import com.rxdroid.repository.model.Resource;
import com.rxdroid.repository.model.Status;
import com.rxdroid.repository.model.User;

import java.util.List;

import fup.prototype.robprototype.view.base.adapters.ObserverAdapter;
import fup.prototype.robprototype.view.base.viewmodels.BaseLiveDataViewModel;
import fup.prototype.robprototype.view.base.viewmodels.ViewState;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class DetailViewModel extends BaseLiveDataViewModel {

    private static final String TAG = "DetailViewModel";

    public ObservableField<String> userName = new ObservableField<>();
    public ObservableArrayList<Repository> items = new ObservableArrayList<>();

    private User user;

    @NonNull
    private final GithubDetailsUiRepository detailsUiRepository;


    public DetailViewModel(@NonNull final GithubDetailsUiRepository detailsUiRepository) {
        this.detailsUiRepository = detailsUiRepository;
    }

    public void loadOrShowData() {
        if (user != null) {
            if (detailsUiRepository.hasValidCacheValue(user.getLogin())) {
                if (detailsUiRepository.getCachedValue().requestError != null) {
                    items.clear();
                    handleErrorCase(detailsUiRepository.getCachedValue().requestError);
                } else {
                    handleSuccessCase(detailsUiRepository.getCachedValue().data);
                }
            } else {
                Observable.concat(getLoadingObservable(), detailsUiRepository.loadBySearchValue(user.getLogin()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RepositoryObserver());
            }
        }
    }

    private Observable<Resource<List<Repository>>> getLoadingObservable() {
        return Observable.just(new Resource<List<Repository>>(Status.LOADING, null, null));
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

    private void storeToDatabase(List<Repository> repositories) {
        final Completable completable = detailsUiRepository.updateDatabase(repositories, user.getId());
        completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DatabaseWriteObserver());
    }

    private void showRepositories(final List<Repository> repositories) {
        items.clear();
        items.addAll(repositories);
    }

    private class DatabaseWriteObserver extends DisposableCompletableObserver {

        @Override
        public void onError(final Throwable e) {
            //Database write transaction failed due of reasons ...
            Log.e(TAG, "DatabaseWriteObserver - onError: ", e);
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "DatabaseWriteObserver - onComplete: ");
        }
    }

    private class RepositoryObserver extends ObserverAdapter<Resource<List<Repository>>> {

        @Override
        public void onSubscribe(final Disposable d) {
            getCompositeDisposable().add(d);
        }

        @Override
        public void onNext(final Resource<List<Repository>> listResource) {
            changeLoadingState(listResource.status == Status.LOADING);
            switch (listResource.status) {
                case ERROR:
                    items.clear();
                    handleErrorCase(listResource.requestError);
                    break;
                case SUCCESS:
                    storeToDatabase(listResource.data);
                    handleSuccessCase(listResource.data);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onError(final Throwable e) {
            Log.e(TAG, "onError: ", e);
            items.clear();
            changeLoadingState(false);
            handleErrorCase(RequestError.create(null, e));
        }

    }


}
