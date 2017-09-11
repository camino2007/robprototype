package fup.prototype.robprototype.view.viewmodels;


import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import javax.inject.Inject;

import fup.prototype.domain.api.LoadingState;
import fup.prototype.domain.api.RequestError;
import fup.prototype.robprototype.ProtoApplication;
import fup.prototype.robprototype.data.repositories.UserRepository;
import fup.prototype.robprototype.model.Repository;
import fup.prototype.robprototype.model.User;
import io.reactivex.annotations.NonNull;

public class MainViewModel extends BaseViewModel implements Parcelable {

    private static final String TAG = "MainViewModel";

    public ObservableBoolean isProgress = new ObservableBoolean(false);
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> observableSearchValue = new ObservableField<>();
    public ObservableArrayList<Repository> items = new ObservableArrayList<>();

    private User user;
    private String searchValue;

    @Inject
    UserRepository userRepository;

    public MainViewModel() {
        this.userRepository.setUserListener(new UserListener());
        observableSearchValue.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                searchValue = observableSearchValue.get();
            }
        });
    }

    @Override
    protected void injectDependencies() {
        ProtoApplication.getAppComponent().inject(this);
    }

    public void loadData() {
        loadUserFromRepository();
    }

    public void showData() {
        if (user != null) {
            userName.set(user.getName());
            observableSearchValue.set(searchValue);
            items.clear();
            items.addAll(user.getRepositoryList());
        }
    }

    private void loadUserFromRepository() {
        userRepository.load(searchValue);
    }


    private class UserListener implements UserRepository.OnUserListener {

        @Override
        public void onUserLoaded(@NonNull final User u) {
            Log.d(TAG, "onUserLoaded - user.getName(): " + u.getName());
            user = u;
            showData();
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.searchValue);
    }

    private MainViewModel(Parcel in) {
        this.user = in.readParcelable(User.class.getClassLoader());
        this.searchValue = in.readString();
    }

    public static final Parcelable.Creator<MainViewModel> CREATOR = new Parcelable.Creator<MainViewModel>() {
        @Override
        public MainViewModel createFromParcel(Parcel source) {
            return new MainViewModel(source);
        }

        @Override
        public MainViewModel[] newArray(int size) {
            return new MainViewModel[size];
        }
    };
}
