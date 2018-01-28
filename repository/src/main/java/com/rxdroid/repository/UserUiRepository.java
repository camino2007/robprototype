package com.rxdroid.repository;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import com.rxdroid.api.RequestError;
import com.rxdroid.api.github.model.GitHubUserModel;
import com.rxdroid.api.github.provider.GitHubUserProvider;
import com.rxdroid.repository.cache.UserCache;
import com.rxdroid.repository.model.User;
import com.rxdroid.repository.model.UserResponse;
import fup.prototype.data.DatabaseAdapter;
import fup.prototype.data.DatabaseState;
import fup.prototype.data.search.UserDatabaseProvider;
import fup.prototype.data.search.UserDto;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Response;

@Singleton
public class UserUiRepository implements UiRepository<UserResponse> {

    private static final String TAG = "UserRepository";

    private final GitHubUserProvider gitHubUserProvider;
    private final UserDatabaseProvider userDatabaseProvider;

    private String lastSearchValue;
    private User user = null;
    private UserCache userCache;

    @Inject
    public UserUiRepository(@NonNull final GitHubUserProvider gitHubUserProvider, @NonNull final UserDatabaseProvider userDatabaseProvider) {
        this.gitHubUserProvider = gitHubUserProvider;
        this.userDatabaseProvider = userDatabaseProvider;
        //this.userDatabaseProvider.setDatabaseListener(new DatabaseUserAdapter());
        userCache = new UserCache(10L, TimeUnit.SECONDS);
    }

/*      @Override
    public void loadFromDatabase(@NonNull final String userName) {
        final Maybe<UserDto> maybe = userDatabaseProvider.getForSearchValue(userName);
        userDatabaseProvider.executeRead(maybe, Schedulers.io(), AndroidSchedulers.mainThread());
    }*/

    @Override
    public Observable<UserResponse> loadBySearchValue(@NonNull final String searchValue) {
        Log.d(TAG, "loadBySearchValue: hasCachedValue: " + hasCachedValue());
        if (TextUtils.isEmpty(searchValue)) {
            return Observable.just(UserResponse.create(null, RequestError.create(RequestError.ERROR_CODE_NO_SEARCH_INPUT)));
        }
        if (hasValidCacheValue(searchValue)) {
            return Observable.just(UserResponse.create(user, null));
        } else {
            lastSearchValue = searchValue;
            return gitHubUserProvider.loadBySearchValue(searchValue).map(new Function<Response<GitHubUserModel>, UserResponse>() {
                @Override
                public UserResponse apply(final Response<GitHubUserModel> gitHubUserModelResponse) throws Exception {
                    if (gitHubUserModelResponse != null && gitHubUserModelResponse.isSuccessful()) {
                        final User newUser = User.fromApi(gitHubUserModelResponse.body());
                        //insertOrUpdateDatabase(newUser);
                        userCache.setData(newUser);
                        user = newUser;
                        return UserResponse.create(user, null);
                    } else {
                        return UserResponse.create(null, RequestError.create(gitHubUserModelResponse, null));
                    }
                }
            });
        }
    }

    @Override
    public boolean hasCachedValue() {
        return user != null && userCache.isSameObjectCached(user) && userCache.isCacheValid();
    }

    private boolean hasValidCacheValue(@NonNull final String currentSearchValue) {
        return user != null && userCache.isSameObjectCached(user) && userCache.isCacheValid() && TextUtils.equals(lastSearchValue, currentSearchValue);
    }

    public User getUser() {
        return user;
    }

    private void insertOrUpdateDatabase(final User user) {
        final UserDto userDto = new UserDto();
        userDto.name = user.getName();
        userDto.login = user.getLogin();
        userDto.publicRepoCount = user.getPublicRepoCount();
        userDto.publicGistCount = user.getPublicGistCount();
        Completable completable = userDatabaseProvider.insertOrUpdate(userDto);
        // userDatabaseProvider.executeWrite(completable, Schedulers.io(), Schedulers.io());
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
}

