package fup.prototype.robprototype.view.viewmodels;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;

import javax.inject.Inject;

import fup.prototype.domain.api.LoadingState;
import fup.prototype.domain.api.RequestError;
import fup.prototype.robprototype.data.repositories.UserRepository;
import fup.prototype.robprototype.model.User;
import io.reactivex.annotations.NonNull;

public class MainViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";

    public final ObservableBoolean isProgress = new ObservableBoolean(false);
    public final ObservableField<String> userName = new ObservableField<>();

    @Inject
    UserRepository userRepository;

    private MutableLiveData<User> userData = new MutableLiveData<>();

/*    @Nullable
    private MutableLiveData<List<Repository>> results = new MutableLiveData<>();*/

    @Inject
    public MainViewModel(@NonNull final UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userRepository.setUserListener(new UserListener());
        loadUserFromRepository();
       /* if (userData.getValue() == null) {
            loadUserFromRepository();
        } else {
            Log.d(TAG, "loadOrDisplay - userData.getValue() == NULL");
        }*/
    }

    public MutableLiveData<User> getUserData() {
        return userData;
    }

    private void loadUserFromRepository() {
        userRepository.load("camino2007");
    }

    private class UserListener implements UserRepository.OnUserListener {

        @Override
        public void onUserLoaded(@NonNull final User user) {
            Log.d(TAG, "onUserLoaded");
            userData.postValue(user);
            userName.set(user.getName());
        }

        @Override
        public void onError(@NonNull RequestError requestError) {
            Log.e(TAG, "onError: ");
        }

        @Override
        public void onLoadingStateChanged(@NonNull LoadingState loadingState) {
            Log.d(TAG, "onLoadingStateChanged: " + loadingState);
            isProgress.set(loadingState == LoadingState.LOADING);
        }

    }
}
