package fup.prototype.robprototype.search;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.rxdroid.api.LoadingState;
import com.rxdroid.api.RequestError;
import com.rxdroid.repository.UserRepository;
import com.rxdroid.repository.model.User;
import fup.prototype.robprototype.ProtoApplication;
import fup.prototype.robprototype.view.base.viewmodels.BaseViewModel;
import fup.prototype.robprototype.view.base.viewmodels.ViewState;
import javax.inject.Inject;

public class MainViewModel extends BaseViewModel {

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

    private void showUserData(final User user) {
        if (user != null) {
            userName.set(user.getName());
            items.clear();
            items.add(user);
        }
    }

    private void loadUserFromRepository() {
        if (!TextUtils.isEmpty(searchValue.get())) {
            if (userRepository.hasCachedValue()) {
                handleSuccessCase(userRepository.getUser());
            } else {
                userRepository.loadFromApi(searchValue.get());
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
