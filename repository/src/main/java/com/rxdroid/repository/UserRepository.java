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
import com.rxdroid.repository.model.User;
import fup.prototype.data.DatabaseAdapter;
import fup.prototype.data.DatabaseState;
import fup.prototype.data.main.UserDatabaseProviderImpl;
import fup.prototype.data.main.UserEntity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Response;

@Singleton
public class UserRepository implements Repository {

    private static final String TAG = "UserRepository";

    private final ApiGitHubProviderImpl apiGitHubProvider;
    private final UserDatabaseProviderImpl userDatabaseProvider;

    private OnUserListener userListener;
    private String currentSearchValue;
    private User user = null;

    @Inject
    public UserRepository(@NonNull final ApiGitHubProviderImpl apiGitHubProvider, @NonNull final UserDatabaseProviderImpl userDatabaseProvider) {
        this.apiGitHubProvider = apiGitHubProvider;
        this.apiGitHubProvider.setApiCallListener(new ApiUserAdapter());
        this.userDatabaseProvider = userDatabaseProvider;
        this.userDatabaseProvider.setDatabaseListener(new DatabaseUserAdapter());
    }

    @Override
    public void loadFromApi(@NonNull final String userName) {
        currentSearchValue = userName;
        final Observable<Response<GitHubUserModel>> observable = apiGitHubProvider.loadGitHubUser(userName);
        apiGitHubProvider.execute(observable, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Override
    public void loadFromDatabase(@NonNull final String userName) {
        final UserEntity userEntity = userDatabaseProvider.loadForSearchValue(userName);
        //userDatabaseProvider.executeRead(observable, Schedulers.io(), AndroidSchedulers.mainThread());
        final User user = User.fromEntity(userEntity);
        Log.d(TAG, "loadFromDatabase - user.getLogin(): " + user.getLogin());
    }

    @Override
    public boolean hasCachedValue() {
        return false;
    }

    public User getUser() {
        return user;
    }

    public void setUserListener(final OnUserListener userListener) {
        this.userListener = userListener;
    }

    private void insertOrUpdateDatabase(final User user) {
        final UserEntity userEntity = new UserEntity.Builder().login(user.getLogin())
                                                              .name(user.getName())
                                                              .publicGistCount(user.getPublicGistCount())
                                                              .publicRepoCount(user.getPublicRepoCount())
                                                              .build();
        userDatabaseProvider.storeOrUpdate(userEntity);
    /*    final Observable<Boolean> observable = userDatabaseProvider.storeOrUpdate(userEntity);
        userDatabaseProvider.executeWrite(observable, Schedulers.trampoline(), Schedulers.trampoline());*/
    }

    public void load(@Nullable final String searchValue) {
        if (!TextUtils.isEmpty(searchValue)) {
            //  loadSingleUser(searchValue);
        } else {
            //loadAllUser();
        }
    }

/*    private void loadSingleUser(final String searchValue) {
        currentSearchValue = searchValue;
        if (hasCachedValue()){

        } else{

        }
        if (userCache.isSameUserCached(searchValue) && userCache.isCacheValid()) {
            if (userListener != null) {
                userListener.onUserLoaded(userCache.getData());
                return;
            }
        }
        currentSearchValue = searchValue;
        apiGitHubProvider.loadGitHubUser(searchValue);
    }*/

 /*

    public void load(@Nullable final String searchValue) {
        if (!TextUtils.isEmpty(searchValue)) {
            loadSingleUser(searchValue);
        } else {
            loadAllUser();
        }
    }

    private void loadAllUser() {
        //TODO
    }

    private void loadSingleUser(@NonNull final String searchValue) {
        if (userCache.isSameUserCached(searchValue) && userCache.isCacheValid()) {
            if (userListener != null) {
                userListener.onUserLoaded(userCache.getData());
                return;
            }
        }
        currentSearchValue = searchValue;
        apiGitHubProvider.loadGitHubUser(searchValue);
    }

*/

    private void handleErrorCase(@NonNull final RequestError requestError) {
       /* if (userCache.isSameUserCached(currentSearchValue) && userCache.isCacheValid()) {
            if (userListener != null) {
                userListener.onUserLoaded(userCache.getData());
            }
        } else {
            final UserEntity userEntity = userRealmProvider.getForStringValue(currentSearchValue);

            if (userEntity == null) {
                if (userListener != null) {
                    userListener.onError(requestError);
                }
            } else {
                final User user = User.fromEntity(userEntity);
                if (userListener != null) {
                    userListener.onUserLoaded(user);
                }
            }
        }*/
    }

    public interface OnUserListener {

        void onUserLoaded(@Nullable final User user);

        void onError(@NonNull final RequestError requestError);

        void onLoadingStateChanged(@NonNull final LoadingState loadingState);
    }

    private class DatabaseUserAdapter extends DatabaseAdapter<UserEntity> {

        @Override
        public void onDatabaseStateChanged(final DatabaseState databaseState) {
            Log.d(TAG, "onDatabaseStateChanged: " + databaseState);
        }

        @Override
        public void onStoreOrUpdateDatabaseDone(final boolean isSuccess) {
            Log.d(TAG, "onStoreOrUpdateDatabaseDone: " + isSuccess);
            if (isSuccess) {
                loadFromDatabase(currentSearchValue);
            }
        }

        @Override
        public void onLoadDone(final UserEntity userEntity) {
            Log.d(TAG, "onLoadDone: " + userEntity.getLogin());
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
                final User user = User.fromApi(gitHubUserModelResponse.body());
                insertOrUpdateDatabase(user);
                //   userCache.setData(user);
                if (userListener != null) {
                    userListener.onUserLoaded(user);
                }
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

