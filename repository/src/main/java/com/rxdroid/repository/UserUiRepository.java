package com.rxdroid.repository;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.rxdroid.api.error.RequestError;
import com.rxdroid.api.github.model.GitHubUserModel;
import com.rxdroid.api.github.provider.GitHubUserProvider;
import com.rxdroid.repository.cache.UserCache;
import com.rxdroid.repository.model.Resource;
import com.rxdroid.repository.model.User;

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
public class UserUiRepository implements UiRepository<User> {

    private static final String TAG = "UserUiRepository";

    @NonNull
    private final GitHubUserProvider gitHubUserProvider;

    @NonNull
    private final UserDatabaseProvider userDatabaseProvider;

    private String lastSearchValue;
    private UserCache userCache;

    private Resource<User> userResource;


    @Inject
    public UserUiRepository(@NonNull final GitHubUserProvider gitHubUserProvider, @NonNull final UserDatabaseProvider userDatabaseProvider) {
        this.gitHubUserProvider = gitHubUserProvider;
        this.userDatabaseProvider = userDatabaseProvider;
        userCache = new UserCache(10L, TimeUnit.SECONDS);
    }

    @Override
    public Observable<Resource<User>> loadBySearchValue(@NonNull String searchValue) {
        Log.d(TAG, "loadBySearchValue: " + searchValue);
        if (TextUtils.isEmpty(searchValue)) {
            userResource = Resource.error(RequestError.create(RequestError.ERROR_CODE_NO_SEARCH_INPUT), null);
            return Observable.just(userResource);
        }
        if (hasValidCacheValue(searchValue) && userResource.data != null) {
            userResource = Resource.success(userResource.data);
            return Observable.just(userResource);
        }
        lastSearchValue = searchValue;
        return gitHubUserProvider.loadBySearchValue(searchValue).map(new Function<Response<GitHubUserModel>, Resource<User>>() {
            @Override
            public Resource<User> apply(final Response<GitHubUserModel> gitHubUserModelResponse) {
                if (gitHubUserModelResponse != null && gitHubUserModelResponse.isSuccessful()) {
                    final User user = User.fromApi(gitHubUserModelResponse.body());
                    userCache.setData(user);
                    userResource = Resource.success(user);
                } else {
                    final RequestError requestError = RequestError.create(gitHubUserModelResponse, null);
                    userResource = Resource.error(requestError, null);
                }
                return userResource;
            }
        });
    }


    @Override
    public Resource<User> getCachedValue() {
        return userResource;
    }

    public boolean hasValidCacheValue(@NonNull final String currentSearchValue) {
        return userResource != null && TextUtils.equals(lastSearchValue, currentSearchValue) && userCache.hasValidCachedData();
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

