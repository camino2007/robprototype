package com.rxdroid.repository;

import android.support.annotation.NonNull;

import com.rxdroid.repository.model.Resource;

import io.reactivex.Observable;

public interface UiRepository<T> {

    Observable<Resource<T>> loadBySearchValue(@NonNull final String searchValue);

    Resource<T> getCachedValue();

}
