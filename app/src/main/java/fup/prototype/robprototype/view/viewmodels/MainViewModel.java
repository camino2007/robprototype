package fup.prototype.robprototype.view.viewmodels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;
import fup.prototype.domain.api.LoadingState;
import fup.prototype.domain.api.RequestError;
import fup.prototype.robprototype.ProtoApplication;
import fup.prototype.robprototype.data.repositories.UserRepository;
import fup.prototype.robprototype.model.Repository;
import fup.prototype.robprototype.model.User;
import io.reactivex.annotations.NonNull;
import javax.inject.Inject;

public class MainViewModel extends BaseViewModel {

    private static final String TAG = "MainViewModel";

    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> searchValue = new ObservableField<>();
    public ObservableArrayList<Repository> items = new ObservableArrayList<>();

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
            items.addAll(user.getRepositoryList());
        }
    }

    private void loadUserFromRepository() {
        if (!TextUtils.isEmpty(searchValue.get())) {
            userRepository.load(searchValue.get());
        }
    }

    private class UserListener implements UserRepository.OnUserListener {

        @Override
        public void onUserLoaded(@NonNull final User user) {
            Log.d(TAG, "onUserLoaded - user.getName(): " + user.getName());
            showUserData(user);
        }

        @Override
        public void onError(@NonNull RequestError requestError) {
            Log.e(TAG, "onError: ");
            onDataError(requestError);
        }

        @Override
        public void onLoadingStateChanged(@NonNull LoadingState loadingState) {
            changeLoadingState(loadingState == LoadingState.LOADING);
        }
    }
}
