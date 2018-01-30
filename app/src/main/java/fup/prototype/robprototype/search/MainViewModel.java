package fup.prototype.robprototype.search;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jakewharton.rxrelay2.PublishRelay;
import com.rxdroid.api.RequestError;
import com.rxdroid.repository.UserUiRepository;
import com.rxdroid.repository.model.User;
import com.rxdroid.repository.model.UserResponse;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.Reusable;
import fup.prototype.robprototype.view.base.adapters.ObserverAdapter;
import fup.prototype.robprototype.view.base.viewmodels.BaseViewModel;
import fup.prototype.robprototype.view.base.viewmodels.ViewState;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

@Reusable
public class MainViewModel extends BaseViewModel {

    private static final String TAG = "MainViewModel";

    private static final int MIN_LENGTH_SEARCH = 3;
    private static final int DEBOUNCE_TIME_OUT = 650;

    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> searchValue = new ObservableField<>();
    public ObservableArrayList<User> items = new ObservableArrayList<>();

    private PublishRelay<String> publishRelay = PublishRelay.create();

    @NonNull
    private final UserUiRepository userUiRepository;

    @Inject
    public MainViewModel(@NonNull  final UserUiRepository userUiRepository) {
        this.userUiRepository = userUiRepository;
        addRepositoryDisposable();
    }

    private void addRepositoryDisposable() {
        publishRelay.debounce(DEBOUNCE_TIME_OUT, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(final String searchValue) throws Exception {
                        return searchValue.length() >= MIN_LENGTH_SEARCH;
                    }
                })
                .switchMap(new Function<String, Observable<UserResponse>>() {
                    @Override
                    public Observable<UserResponse> apply(final String searchValue) throws Exception {
                        changeLoadingState(true);
                        return userUiRepository.loadBySearchValue(searchValue);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new UserObserver());
    }

    @Override
    public void loadOrShowData() {
        if (userUiRepository.hasValidCacheValue(searchValue.get())) {
            if (userUiRepository.getCachedValue().getRequestError() != null) {
                items.clear();
                handleErrorCase(userUiRepository.getCachedValue().getRequestError());
            } else {
                handleSuccessCase(userUiRepository.getCachedValue().getUser());
            }
        }
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
            userName.set(user.getName());
            items.clear();
            items.add(user);
        }
    }

    public void updateSearchInput(final String search) {
        searchValue.set(search);
        publishRelay.accept(search);
    }

    private class UserObserver extends ObserverAdapter<UserResponse> {

        @Override
        public void onSubscribe(final Disposable d) {
            getCompositeDisposable().add(d);
        }

        @Override
        public void onNext(final UserResponse userResponse) {
            changeLoadingState(false);
            if (userResponse.hasError()) {
                handleErrorCase(userResponse.getRequestError());
            } else {
                handleSuccessCase(userResponse.getUser());
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
