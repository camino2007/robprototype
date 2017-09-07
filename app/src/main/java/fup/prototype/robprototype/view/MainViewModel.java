package fup.prototype.robprototype.view;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import fup.prototype.domain.api.LoadingState;
import fup.prototype.domain.api.RequestError;
import fup.prototype.robprototype.model.Repository;
import fup.prototype.robprototype.model.User;
import fup.prototype.robprototype.model.UserRepository;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class MainViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";

    public final ObservableBoolean isProgress = new ObservableBoolean(false);
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableList<Repository> items = new ObservableArrayList<>();

    @Inject
    UserRepository userRepository;

    @Nullable
    private LiveData<User> userLiveData;

    @Nullable
    private MutableLiveData<List<Repository>> results = new MutableLiveData<>();

    @Inject
    public MainViewModel(@NonNull final UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userRepository.setUserListener(new UserListener());
        if (results.getValue() == null) {
            loadOrDisplay();
        }
    }

    public void loadOrDisplay() {
        if (results.getValue() != null) {
            return;
        } else {
            Log.d(TAG, "loadOrDisplay - results == NULL");
        }
        loadUserFromRepository();
    }


    public LiveData<List<Repository>> getResults() {
        return results;
    }

    private void loadUserFromRepository() {
        userRepository.load("camino2007");
    }

    private class UserListener implements UserRepository.OnUserListener {

        @Override
        public void onUserLoaded(@NonNull final User user) {
            Log.d(TAG, "onUserLoaded");
            results.postValue(user.getRepositoryList());
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
