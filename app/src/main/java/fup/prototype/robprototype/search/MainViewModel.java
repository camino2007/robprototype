package fup.prototype.robprototype.search;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.util.Log;
import com.jakewharton.rxrelay2.PublishRelay;
import com.rxdroid.api.RequestError;
import com.rxdroid.repository.UserUiRepository;
import com.rxdroid.repository.model.User;
import com.rxdroid.repository.model.UserResponse;
import fup.prototype.robprototype.ProtoApplication;
import fup.prototype.robprototype.view.base.viewmodels.BaseViewModel;
import fup.prototype.robprototype.view.base.viewmodels.ViewState;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class MainViewModel extends BaseViewModel {

    private static final String TAG = "MainViewModel";

    private static final int MIN_LENGTH_SEARCH = 3;
    private static final int DEBOUNCE_TIME_OUT = 650;

    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> searchValue = new ObservableField<>();
    public ObservableArrayList<User> items = new ObservableArrayList<>();

    private PublishRelay<String> publishRelay = PublishRelay.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    UserUiRepository userRepository;

    public MainViewModel() {
        publishRelay.debounce(DEBOUNCE_TIME_OUT, TimeUnit.MILLISECONDS).filter(new Predicate<CharSequence>() {
            @Override
            public boolean test(final CharSequence charSequence) throws Exception {
                return charSequence.length() >= MIN_LENGTH_SEARCH;
            }
        }).distinctUntilChanged().switchMap(new Function<String, Observable<UserResponse>>() {
            @Override
            public Observable<UserResponse> apply(final String searchValue) throws Exception {
                Log.d(TAG, "MainViewModel - apply - searchValue: " + searchValue);
                changeLoadingState(true);
                return userRepository.loadBySearchValue(searchValue);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new UserObserver());
    }

    @Override
    protected void injectDependencies() {
        ProtoApplication.getAppComponent().inject(this);
    }

    @Override
    public void loadOrShowData() {
        Log.d(TAG, "loadOrShowData: ");
    }

    public PublishRelay<String> getPublishRelay() {
        return publishRelay;
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

    private class UserObserver implements Observer<UserResponse> {

        @Override
        public void onSubscribe(final Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(final UserResponse userResponse) {
            changeLoadingState(false);
            if (userResponse.getRequestError() != null) {
                items.clear();
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

        @Override
        public void onComplete() {
        }
    }
}
