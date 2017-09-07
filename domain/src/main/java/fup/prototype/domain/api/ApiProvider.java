package fup.prototype.domain.api;

import android.util.Log;

import fup.prototype.domain.github.GithubApi;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public abstract class ApiProvider<T> {

    private static final String TAG = "ApiProvider";

    @NonNull
    private final GithubApi githubApi;

    private LoadingState loadingState = LoadingState.IDLE;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private OnApiCallListener<T> apiCallListener;

    public ApiProvider(@NonNull final GithubApi githubApi) {
        this.githubApi = githubApi;
    }

    @NonNull
    public GithubApi getGithubApi() {
        return githubApi;
    }

    public LoadingState getLoadingState() {
        return loadingState;
    }

    public void setApiCallListener(final OnApiCallListener<T> apiCallListener) {
        this.apiCallListener = apiCallListener;
    }

    public void execute(Observable<Response<T>> observable) {
        if (getLoadingState() != LoadingState.LOADING) {
            loadingStateChanged(LoadingState.LOADING);
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiResponseObserver());
        } else {
            //TODO evaluate, what do to when an API call is already in progress
        }
    }

   public void executeZip(Observable<T> observable) {
        if (getLoadingState() != LoadingState.LOADING) {
            loadingStateChanged(LoadingState.LOADING);
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver());
        } else {
            //TODO evaluate, what do to when an API call is already in progress
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

    private class ApiResponseObserver implements Observer<Response<T>> {

        @Override
        public void onSubscribe(@NonNull final Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(@NonNull final Response<T> response) {
            if (response.isSuccessful()) {
                loadingStateChanged(LoadingState.DONE);
                apiCallDone(response.body());
            } else {
                loadingStateChanged(LoadingState.ERROR);
                apiCallError(RequestError.create(response, null));
            }
        }

        @Override
        public void onError(@NonNull final Throwable e) {
            loadingStateChanged(LoadingState.ERROR);
            Log.e(TAG, "onError: ", e);
            apiCallError(RequestError.create(null, e));
        }

        @Override
        public void onComplete() {

        }
    }
}
