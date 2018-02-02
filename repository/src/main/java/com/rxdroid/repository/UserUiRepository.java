package com.rxdroid.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.rxdroid.api.error.RequestError;
import com.rxdroid.api.github.model.GitHubUserModel;
import com.rxdroid.api.github.provider.GitHubUserProvider;
import com.rxdroid.repository.cache.UserCache;
import com.rxdroid.repository.model.User;
import com.rxdroid.repository.model.UserResponse;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import fup.prototype.data.search.UserDatabaseProvider;
import fup.prototype.data.search.UserDto;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Response;

@Singleton
public class UserUiRepository implements UiRepository<UserResponse> {

    private static final String TAG = "UserUiRepository";

    @NonNull
    private final GitHubUserProvider gitHubUserProvider;

    @NonNull
    private final UserDatabaseProvider userDatabaseProvider;

    private String lastSearchValue;
    private UserCache userCache;

    private UserResponse userResponse;

    @Inject
    public UserUiRepository(@NonNull final GitHubUserProvider gitHubUserProvider, @NonNull final UserDatabaseProvider userDatabaseProvider) {
        this.gitHubUserProvider = gitHubUserProvider;
        this.userDatabaseProvider = userDatabaseProvider;
        userCache = new UserCache(10L, TimeUnit.SECONDS);
    }

    @Override
    public Observable<UserResponse> loadBySearchValue(@NonNull final String searchValue) {
        Log.d(TAG, "loadBySearchValue: " + searchValue);
        if (TextUtils.isEmpty(searchValue)) {
            return Observable.just(UserResponse.create(null, RequestError.create(RequestError.ERROR_CODE_NO_SEARCH_INPUT)));
        }
        if (hasValidCacheValue(searchValue)) {
            return Observable.just(UserResponse.create(userResponse.getUser(), null));
        }
        lastSearchValue = searchValue;
        return gitHubUserProvider.loadBySearchValue(searchValue).map(new Function<Response<GitHubUserModel>, UserResponse>() {
            @Override
            public UserResponse apply(final Response<GitHubUserModel> gitHubUserModelResponse) throws Exception {
                if (gitHubUserModelResponse != null && gitHubUserModelResponse.isSuccessful()) {
                    final User user = User.fromApi(gitHubUserModelResponse.body());
                    userCache.setData(user);
                    userResponse = createUserResponse(user, null);
                } else {
                    userResponse = createUserResponse(null, RequestError.create(gitHubUserModelResponse, null));
                }
                return userResponse;
            }
        });
    }


    @Override
    public UserResponse getCachedValue() {
        return userResponse;
    }

    private UserResponse createUserResponse(@Nullable User user, @Nullable RequestError requestError) {
        return UserResponse.create(user, requestError);
    }

    public boolean hasValidCacheValue(@NonNull final String currentSearchValue) {
        return userResponse != null && TextUtils.equals(lastSearchValue, currentSearchValue) && userCache.hasValidCachedData();
    }

    public Completable updateDatabase(@NonNull final User user) {
        final UserDto userDto = new UserDto();
        userDto.name = user.getName();
        userDto.login = user.getLogin();
        userDto.publicRepoCount = user.getPublicRepoCount();
        userDto.publicGistCount = user.getPublicGistCount();
        return userDatabaseProvider.insertOrUpdate(userDto);
    }


}

