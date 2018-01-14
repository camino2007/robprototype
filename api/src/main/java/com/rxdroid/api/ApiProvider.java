package com.rxdroid.api;

import android.util.Log;
import com.rxdroid.api.github.GitHubApi;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class ApiProvider<T> {

    private static final String TAG = "ApiProvider";

    @NonNull
    private final GitHubApi gitHubApi;

    private LoadingState loadingState = LoadingState.IDLE;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private OnApiCallListener<T> apiCallListener;

    public ApiProvider(@NonNull final GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    @NonNull
    public GitHubApi getGitHubApi() {
        return gitHubApi;
    }

    public LoadingState getLoadingState() {
        return loadingState;
    }

    public void setApiCallListener(final OnApiCallListener<T> apiCallListener) {
        this.apiCallListener = apiCallListener;
    }

    public void execute(@NonNull final Observable<T> observable, @NonNull final Scheduler subscribeOnScheduler, @NonNull final Scheduler observeOnScheduler) {
        Log.d(TAG, "execute - getLoadingState(): " + getLoadingState());
        if (getLoadingState() != LoadingState.LOADING) {
            loadingStateChanged(LoadingState.LOADING);
            observable.subscribeOn(subscribeOnScheduler).observeOn(observeOnScheduler).subscribe(new ApiObserver());
        } else {
            notifyProviderBusy();
        }
    }

    private void notifyProviderBusy() {
        if (apiCallListener != null) {
            apiCallListener.onProviderIsBusy();
        }
    }

    public void cancel() {
        compositeDisposable.dispose();
    }

    protected void loadingStateChanged(final LoadingState state) {
        if (apiCallListener != null) {
            apiCallListener.onLoadingStateChanged(state);
        }
    }

    private void apiCallDone(final T t) {
        if (apiCallListener != null) {
            apiCallListener.onApiCallDone(t);
        }
    }

    private void apiCallError(@NonNull final RequestError requestError) {
        if (apiCallListener != null) {
            apiCallListener.onApiCallError(requestError);
        }
    }

    private class ApiObserver implements Observer<T> {

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(@NonNull T t) {
            loadingStateChanged(LoadingState.DONE);
            apiCallDone(t);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            loadingStateChanged(LoadingState.ERROR);
            apiCallError(RequestError.create(null, e));
        }

        @Override
        public void onComplete() {

        }
    }
}
