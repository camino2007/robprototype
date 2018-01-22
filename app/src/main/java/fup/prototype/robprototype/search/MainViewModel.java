package fup.prototype.robprototype.search;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.rxdroid.api.LoadingState;
import com.rxdroid.api.RequestError;
import com.rxdroid.repository.UserRepository;
import com.rxdroid.repository.model.User;
import fup.prototype.robprototype.ProtoApplication;
import fup.prototype.robprototype.view.base.viewmodels.BaseViewModel;
import fup.prototype.robprototype.view.base.viewmodels.ViewState;
import javax.inject.Inject;

public class MainViewModel extends BaseViewModel {

    private static final String TAG = "MainViewModel";

    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> searchValue = new ObservableField<>();
    public ObservableArrayList<User> items = new ObservableArrayList<>();

    @Inject
    UserRepository userRepository;

    public MainViewModel() {
        this.userRepository.setUserListener(new UserListener());
    }

    @Override
    protected void injectDependencies() {
        ProtoApplication.getAppComponent().inject(this);
    }

    @Override
    public void loadOrShowData() {
        loadUserFromRepository();
    }

    public void loadFromDb() {
        userRepository.loadFromDatabase(searchValue.get());
    }

    private void showUserData(final User user) {
        if (user != null) {
            userName.set(user.getName());
            items.clear();
            items.add(user);
        }
    }

    private void loadUserFromRepository() {
        Log.d(TAG, "loadUserFromRepository: " + searchValue.get());
        if (!TextUtils.isEmpty(searchValue.get())) {
            Log.d(TAG, "loadUserFromRepository - userRepository.hasCachedValue(): " + userRepository.hasCachedValue());
            if (userRepository.hasCachedValue()) {
                handleSuccessCase(userRepository.getUser());
            } else {
                userRepository.load(searchValue.get());
            }
        } else {
            final RequestError requestError = RequestError.create(RequestError.ERROR_CODE_NO_SEARCH_INPUT);
            handleErrorCase(requestError);
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

    private class UserListener implements UserRepository.OnUserListener {

        @Override
        public void onUserLoaded(@Nullable final User user) {
            handleSuccessCase(user);
        }

        @Override
        public void onError(@NonNull final RequestError requestError) {
            handleErrorCase(requestError);
        }

        @Override
        public void onLoadingStateChanged(@NonNull LoadingState loadingState) {
            changeLoadingState(loadingState == LoadingState.LOADING);
        }
    }
}
