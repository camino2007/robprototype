package fup.prototype.data;

import android.content.Context;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;

public abstract class DatabaseProvider<T> {

    private DatabaseState databaseState = DatabaseState.IDLE;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private OnDatabaseListener<T> databaseListener;

    @NonNull
    private AppDatabase appDatabase;

    public DatabaseProvider(@NonNull final Context context) {
        appDatabase = AppDatabase.getInstance(context);
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    @NonNull
    public DatabaseState getDatabaseState() {
        return databaseState;
    }

    public void setDatabaseListener(final OnDatabaseListener<T> databaseListener) {
        this.databaseListener = databaseListener;
    }

    public void executeRead(@NonNull final Maybe<T> maybe, @NonNull final Scheduler subscribeOnScheduler, @NonNull final Scheduler observeOnScheduler) {
        if (getDatabaseState() != DatabaseState.LOADING) {
            loadingStateChanged(DatabaseState.LOADING);
            maybe.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler).subscribe(new ReadObserver());
        } else {
            notifyProviderBusy();
        }
    }

    public void executeWrite(@NonNull final Completable completable,
                             @NonNull final Scheduler subscribeOnScheduler,
                             @NonNull final Scheduler observeOnScheduler) {
        if (getDatabaseState() != DatabaseState.LOADING) {
            loadingStateChanged(DatabaseState.LOADING);
            completable.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler).subscribe(new WriteObserver());
        } else {
            notifyProviderBusy();
        }
    }

    public void executeDelete() {
        //TODO
    }

    private void notifyProviderBusy() {
        if (databaseListener != null) {
            databaseListener.onProviderIsBusy();
        }
    }

    public void cancel() {
        compositeDisposable.dispose();
    }

    protected void loadingStateChanged(final DatabaseState state) {
        if (databaseListener != null) {
            databaseListener.onDatabaseStateChanged(state);
        }
    }

    private void loadDone(final T t) {
        if (databaseListener != null) {
            databaseListener.onLoadDone(t);
        }
    }

    private void loadError() {
        if (databaseListener != null) {
            databaseListener.onLoadError();
        }
    }

    private void wasStoreOrUpdatesSuccessful(final boolean isSuccess) {
        if (databaseListener != null) {
            databaseListener.onStoreOrUpdateDatabaseDone(isSuccess);
        }
    }

    private class WriteObserver extends DisposableCompletableObserver {

        @Override
        public void onError(final Throwable e) {
            loadingStateChanged(DatabaseState.ERROR);
            wasStoreOrUpdatesSuccessful(false);
        }

        @Override
        public void onComplete() {
            loadingStateChanged(DatabaseState.DONE);
            wasStoreOrUpdatesSuccessful(true);
        }
    }

    private class ReadObserver implements MaybeObserver<T> {

        @Override
        public void onSubscribe(final Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onSuccess(final T t) {
            loadingStateChanged(DatabaseState.DONE);
            loadDone(t);
        }

        @Override
        public void onError(final Throwable e) {
            loadingStateChanged(DatabaseState.ERROR);
            loadError();
        }

        @Override
        public void onComplete() {
        }
    }
}
