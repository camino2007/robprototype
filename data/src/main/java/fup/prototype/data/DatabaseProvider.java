package fup.prototype.data;

import android.util.Log;
import fup.prototype.data.realm.RealmService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class DatabaseProvider<T> {

    private static final String TAG = "DatabaseProvider";

    @NonNull
    private final RealmService realmService;

    private DatabaseState databaseState = DatabaseState.IDLE;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private OnDatabaseListener<T> databaseListener;

    public DatabaseProvider(@NonNull final RealmService realmService) {
        this.realmService = realmService;
    }

    @NonNull
    public RealmService getRealmService() {
        return realmService;
    }

    public DatabaseState getDatabaseState() {
        return databaseState;
    }

    public void setDatabaseListener(final OnDatabaseListener<T> databaseListener) {
        this.databaseListener = databaseListener;
    }

    public void executeRead(@NonNull final Observable<T> observable,
                            @NonNull final Scheduler subscribeOnScheduler,
                            @NonNull final Scheduler observeOnScheduler) {
        Log.d(TAG, "executeRead - getLoadingState(): " + getDatabaseState());
        if (getDatabaseState() != DatabaseState.LOADING) {
            loadingStateChanged(DatabaseState.LOADING);
            observable.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler).subscribe(new ReadObserver());
        } else {
            notifyProviderBusy();
        }
    }

    public void executeWrite(@NonNull final Observable<Boolean> observable,
                             @NonNull final Scheduler subscribeOnScheduler,
                             @NonNull final Scheduler observeOnScheduler) {
        Log.d(TAG, "executeWrite - getLoadingState(): " + getDatabaseState());
        if (getDatabaseState() != DatabaseState.LOADING) {
            loadingStateChanged(DatabaseState.LOADING);
            observable.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler).subscribe(new WriteObserver());
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

    private class WriteObserver implements Observer<Boolean> {

        @Override
        public void onSubscribe(final Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(final Boolean aBoolean) {
            loadingStateChanged(DatabaseState.DONE);
            wasStoreOrUpdatesSuccessful(true);
        }

        @Override
        public void onError(final Throwable e) {
            Log.e(TAG, "onError: ", e);
            loadingStateChanged(DatabaseState.ERROR);
            wasStoreOrUpdatesSuccessful(false);
        }

        @Override
        public void onComplete() {

        }
    }

    private class ReadObserver implements Observer<T> {

        @Override
        public void onSubscribe(final Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(final T t) {
            loadingStateChanged(DatabaseState.DONE);
            loadDone(t);
        }

        @Override
        public void onError(final Throwable e) {
            Log.e(TAG, "onError: ", e);
            loadingStateChanged(DatabaseState.ERROR);
            loadError();
        }

        @Override
        public void onComplete() {

        }
    }



}
