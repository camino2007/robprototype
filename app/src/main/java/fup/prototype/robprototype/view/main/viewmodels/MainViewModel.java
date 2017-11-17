package fup.prototype.robprototype.view.main.viewmodels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import fup.prototype.domain.api.LoadingState;
import fup.prototype.domain.api.RequestError;
import fup.prototype.robprototype.ProtoApplication;
import fup.prototype.robprototype.data.repositories.UserRepository;
import fup.prototype.robprototype.view.base.viewmodels.BaseViewModel;
import fup.prototype.robprototype.view.base.viewmodels.ViewState;
import fup.prototype.robprototype.view.main.model.User;
import io.reactivex.annotations.NonNull;
import javax.inject.Inject;

public class MainViewModel extends BaseViewModel {

    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> searchValue = new ObservableField<>();
    public ObservableArrayList<User> items = new ObservableArrayList<>();
    //public ObservableArrayList<Repository> items = new ObservableArrayList<>();

    @Inject
    protected UserRepository userRepository;

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
            userRepository.load(searchValue.get());
        } else {
            final RequestError requestError = RequestError.create(RequestError.ERROR_CODE_NO_SEARCH_INPUT);
            onDataError(requestError);
        }
    }

    private class UserListener implements UserRepository.OnUserListener {

        @Override
        public void onUserLoaded(@Nullable final User user) {
            if (user != null) {
                setViewState(ViewState.ON_LOADED);
                showUserData(user);
            } else {
                setViewState(ViewState.ON_NO_DATA);
            }
        }

        @Override
        public void onError(@NonNull final RequestError requestError) {
            onDataError(requestError);
        }

        @Override
        public void onLoadingStateChanged(@NonNull LoadingState loadingState) {
            changeLoadingState(loadingState == LoadingState.LOADING);
        }
    }
}
