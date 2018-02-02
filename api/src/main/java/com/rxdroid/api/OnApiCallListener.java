package com.rxdroid.api;

import com.rxdroid.api.error.RequestError;

import io.reactivex.annotations.NonNull;

public interface OnApiCallListener<T> {

    void onLoadingStateChanged(@NonNull final LoadingState loadingState);

    void onApiCallDone(@NonNull final T t);

    void onApiCallError(@NonNull final RequestError requestError);

    void onProviderIsBusy();
}
