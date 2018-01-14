package fup.prototype.data;

import io.reactivex.annotations.NonNull;

public interface OnDatabaseListener<T> {

    void onDatabaseStateChanged(@NonNull final DatabaseState databaseState);

    void onStoreOrUpdateDatabaseDone(final boolean isSuccess);

    void onLoadDone(@NonNull final T t);

    void onLoadError();

    void onProviderIsBusy();
}
