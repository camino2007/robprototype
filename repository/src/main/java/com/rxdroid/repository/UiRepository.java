package com.rxdroid.repository;

import android.support.annotation.NonNull;
import io.reactivex.Observable;

public interface UiRepository<T> {

    Observable<T> loadBySearchValue(@NonNull final String searchValue);

    boolean hasCachedValue();
}
