package fup.prototype.robprototype.search;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jakewharton.rxrelay2.PublishRelay;
import com.rxdroid.api.error.RequestError;
import com.rxdroid.repository.UserUiRepository;
import com.rxdroid.repository.model.Resource;
import com.rxdroid.repository.model.Status;
import com.rxdroid.repository.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fup.prototype.robprototype.view.base.adapters.ObserverAdapter;
import fup.prototype.robprototype.view.base.viewmodels.BaseLiveDataViewModel;
import fup.prototype.robprototype.view.base.viewmodels.ViewState;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends BaseLiveDataViewModel {


    private static final int MIN_LENGTH_SEARCH = 3;
    private static final int DEBOUNCE_TIME_OUT = 650;

    private static final String TAG = "NewMainViewModel";

    private MutableLiveData<String> searchValueLiveData = new MutableLiveData<>();
    public MutableLiveData<String> userName = new MutableLiveData<>();
    public MutableLiveData<List<User>> items = new MutableLiveData<>();

    private PublishRelay<String> publishRelay = PublishRelay.create();

    @NonNull
    private final UserUiRepository userUiRepository;

    public MainViewModel(@NonNull final UserUiRepository userUiRepository) {
        this.userUiRepository = userUiRepository;
        addRepositoryDisposable();
    }

    private void addRepositoryDisposable() {
        publishRelay.debounce(DEBOUNCE_TIME_OUT, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(final String searchValue) {
                        return searchValue.length() >= MIN_LENGTH_SEARCH;
                    }
                })
                .switchMap(new Function<String, Observable<Resource<User>>>() {
                    @Override
                    public Observable<Resource<User>> apply(final String searchValue) {
                        return Observable.concat(getLoadingObservable(), userUiRepository.loadBySearchValue(searchValue));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new UserObserver());
    }

    private Observable<Resource<User>> getLoadingObservable() {
        return Observable.just(new Resource<User>(Status.LOADING, null, null));
    }

    public MutableLiveData<String> getSearchValueLiveData() {
        return searchValueLiveData;
    }

    public void updateSearchInput(final String search) {
        searchValueLiveData.postValue(search);
        publishRelay.accept(search);
    }

    private void handleSuccessCase(final User user) {
        if (user != null) {
            setViewState(ViewState.ON_LOADED);
            showUserData(user);
        } else {
            setViewState(ViewState.ON_NO_DATA);
        }
    }

    private void showUserData(final User user) {
        if (user != null) {
            userName.postValue(user.getName());
            final List<User> users = new ArrayList<>();
            users.add(user);
            items.postValue(users);
        }
    }

    private void storeToDatabase(final User user) {
        final Completable completable = userUiRepository.updateDatabase(user);
        completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DatabaseWriteObserver());
    }

    private class UserObserver extends ObserverAdapter<Resource<User>> {

        @Override
        public void onSubscribe(final Disposable d) {
            getCompositeDisposable().add(d);
        }

        @Override
        public void onNext(final Resource<User> userResource) {
            changeLoadingState(userResource.status == Status.LOADING);
            switch (userResource.status) {
                case ERROR:
                    handleErrorCase(userResource.requestError);
                    break;
                case SUCCESS:
                    storeToDatabase(userResource.data);
                    handleSuccessCase(userResource.data);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onError(final Throwable e) {
            Log.e(TAG, "onError: ", e);
            changeLoadingState(false);
            handleErrorCase(RequestError.create(null, e));
        }

    }

    private class DatabaseWriteObserver extends DisposableCompletableObserver {

        @Override
        public void onError(final Throwable e) {
            //Database write transaction failed due of reasons ...
            Log.e(TAG, "DatabaseWriteObserver - onError: ", e);
        }

        @Override
        public void onComplete() {
            // In case you want to know, when db write succeeded
            Log.d(TAG, "DatabaseWriteObserver - onComplete");
        }
    }

}
