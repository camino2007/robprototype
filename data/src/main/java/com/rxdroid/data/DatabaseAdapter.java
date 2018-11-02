package com.rxdroid.data;

import io.reactivex.annotations.NonNull;

/**
 * Empty implementation
 *
 * @param <T> the desired result object as generic
 */
public class DatabaseAdapter<T> implements OnDatabaseListener<T> {

    @Override
    public void onDatabaseStateChanged(@NonNull final DatabaseState databaseState) {

    }

    @Override
    public void onStoreOrUpdateDatabaseDone(final boolean isSuccess) {

    }

    @Override
    public void onLoadDone(@NonNull  final T t) {

    }

    @Override
    public void onLoadError() {

    }

    @Override
    public void onProviderIsBusy() {

    }
}
