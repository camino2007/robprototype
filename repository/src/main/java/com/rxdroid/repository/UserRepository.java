package com.rxdroid.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.rxdroid.api.ApiCallAdapter;
import com.rxdroid.api.LoadingState;
import com.rxdroid.api.RequestError;
import com.rxdroid.api.github.model.GitHubUserModel;
import com.rxdroid.api.github.provider.ApiGitHubProviderImpl;
import com.rxdroid.repository.cache.UserCache;
import com.rxdroid.repository.model.User;
import fup.prototype.data.DatabaseAdapter;
import fup.prototype.data.DatabaseState;
import fup.prototype.data.main.UserDatabaseProviderImpl;
import fup.prototype.data.main.UserDto;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Response;

@Singleton
public class UserRepository implements Repository {

    private static final String TAG = "UserRepository";

    private final ApiGitHubProviderImpl apiGitHubProvider;
    private final UserDatabaseProviderImpl userDatabaseProvider;

    private OnUserListener userListener;
    private User user = null;
    private UserCache userCache;

    @Inject
    public UserRepository(@NonNull final ApiGitHubProviderImpl apiGitHubProvider, @NonNull final UserDatabaseProviderImpl userDatabaseProvider) {
        this.apiGitHubProvider = apiGitHubProvider;
        this.apiGitHubProvider.setApiCallListener(new ApiUserAdapter());
        this.userDatabaseProvider = userDatabaseProvider;
        this.userDatabaseProvider.setDatabaseListener(new DatabaseUserAdapter());
        userCache = new UserCache(10L, TimeUnit.SECONDS);
    }

    @Override
    public void loadFromApi(@NonNull final String searchValue) {
        final Observable<Response<GitHubUserModel>> observable = apiGitHubProvider.loadGitHubUser(searchValue);
        apiGitHubProvider.execute(observable, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Override
    public void loadFromDatabase(@NonNull final String userName) {
        final Maybe<UserDto> maybe = userDatabaseProvider.getForSearchValue(userName);
        userDatabaseProvider.executeRead(maybe, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Override
    public boolean hasCachedValue() {
        return user != null && userCache.isSameObjectCached(user) && userCache.isCacheValid();
    }

    public User getUser() {
        return user;
    }

    public void setUserListener(final OnUserListener userListener) {
        this.userListener = userListener;
    }

    private void insertOrUpdateDatabase(final User user) {
        final UserDto userDto = new UserDto();
        userDto.name = user.getName();
        userDto.login = user.getLogin();
        userDto.publicRepoCount = user.getPublicRepoCount();
        userDto.publicGistCount = user.getPublicGistCount();
        Completable completable = userDatabaseProvider.insertOrUpdate(userDto);
        userDatabaseProvider.executeWrite(completable, Schedulers.io(), Schedulers.io());
    }

    public void load(@Nullable final String searchValue) {
        if (!TextUtils.isEmpty(searchValue)) {
            loadSingleUser(searchValue);
        } else {
            //loadAllUser();
        }
    }

    private void loadSingleUser(final String searchValue) {
        if (!tryToGetUserFromDatabase()) {
            apiGitHubProvider.loadGitHubUser(searchValue);
        }
    }

    private boolean tryToGetUserFromDatabase() {
        if (hasCachedValue()) {
            if (userListener != null) {
                userListener.onUserLoaded(userCache.getData());
                return true;
            }
        }
        return false;
    }

    private void loadAllUser() {
        //TODO
    }

    private void handleErrorCase(@NonNull final RequestError requestError) {
        if (!tryToGetUserFromDatabase()) {
            if (userListener != null) {
                userListener.onError(requestError);
            }
        }
    }

    public interface OnUserListener {

        void onUserLoaded(@Nullable final User user);

        void onError(@NonNull final RequestError requestError);

        void onLoadingStateChanged(@NonNull final LoadingState loadingState);
    }

    private class DatabaseUserAdapter extends DatabaseAdapter<UserDto> {

        @Override
        public void onDatabaseStateChanged(final DatabaseState databaseState) {
            Log.d(TAG, "onDatabaseStateChanged: " + databaseState);
        }

        @Override
        public void onStoreOrUpdateDatabaseDone(final boolean isSuccess) {
            Log.d(TAG, "onStoreOrUpdateDatabaseDone: " + isSuccess);
        }

        @Override
        public void onLoadDone(final UserDto userDto) {
            Log.d(TAG, "onLoadDone: " + userDto.login);
        }

        @Override
        public void onLoadError() {
            Log.d(TAG, "onLoadError: ");
        }

        @Override
        public void onProviderIsBusy() {
            Log.d(TAG, "onProviderIsBusy: ");
        }
    }

    private class ApiUserAdapter extends ApiCallAdapter<Response<GitHubUserModel>> {

        @Override
        public void onLoadingStateChanged(final LoadingState loadingState) {
            if (userListener != null) {
                userListener.onLoadingStateChanged(loadingState);
            }
        }

        @Override
        public void onApiCallDone(@Nullable final Response<GitHubUserModel> gitHubUserModelResponse) {
            Log.d(TAG, "ApiUserAdapter - onApiCallDone: ");
            if (gitHubUserModelResponse != null && gitHubUserModelResponse.isSuccessful()) {
                final User newUser = User.fromApi(gitHubUserModelResponse.body());
                insertOrUpdateDatabase(newUser);
                userCache.setData(newUser);
                if (userListener != null) {
                    userListener.onUserLoaded(newUser);
                }
                user = newUser;
            } else {
                handleErrorCase(RequestError.create(gitHubUserModelResponse, null));
            }
        }

        @Override
        public void onApiCallError(final RequestError requestError) {
            Log.d(TAG, "ApiUserAdapter - onApiCallError: ");
            handleErrorCase(requestError);
        }

        @Override
        public void onProviderIsBusy() {
            Log.d(TAG, "ApiUserAdapter - onProviderIsBusy: ");
        }
    }
}

