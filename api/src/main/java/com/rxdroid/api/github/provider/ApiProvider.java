package com.rxdroid.api.github.provider;

import android.support.annotation.NonNull;
import io.reactivex.Observable;
import retrofit2.Response;

public interface ApiProvider<T> {

    Observable<Response<T>> loadBySearchValue(@NonNull final String searchValue);
}
