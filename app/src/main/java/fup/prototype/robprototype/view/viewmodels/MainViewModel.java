package fup.prototype.robprototype.view.viewmodels;


import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;

import javax.inject.Inject;

import fup.prototype.domain.api.LoadingState;
import fup.prototype.domain.api.RequestError;
import fup.prototype.robprototype.ProtoApplication;
import fup.prototype.robprototype.data.repositories.UserRepository;
import fup.prototype.robprototype.model.Repository;
import fup.prototype.robprototype.model.User;
import io.reactivex.annotations.NonNull;

public class MainViewModel extends BaseViewModel {

    private static final String TAG = "MainViewModel";

    public final ObservableBoolean isProgress = new ObservableBoolean(false);
    public final ObservableField<String> userName = new ObservableField<>();
    public final ObservableArrayList<Repository> items = new ObservableArrayList<>();

    @Inject
    UserRepository userRepository;

    public MainViewModel() {
        this.userRepository.setUserListener(new UserListener());
    }

    @Override
    protected void injectDependencies() {
        ProtoApplication.getAppComponent().inject(this);
    }

    public void loadData() {
        loadUserFromRepository();
    }

    private void loadUserFromRepository() {
        userRepository.load("camino2007");
    }


    private class UserListener implements UserRepository.OnUserListener {

        @Override
        public void onUserLoaded(@NonNull final User user) {
            Log.d(TAG, "onUserLoaded - user.getName(): " + user.getName());
            userName.set(user.getName());
            items.clear();
            items.addAll(user.getRepositoryList());
        }

        @Override
        public void onError(@NonNull RequestError requestError) {
            Log.e(TAG, "onError: ");
        }

        @Override
        public void onLoadingStateChanged(@NonNull LoadingState loadingState) {
            isProgress.set(loadingState == LoadingState.LOADING);
        }

    }
}
