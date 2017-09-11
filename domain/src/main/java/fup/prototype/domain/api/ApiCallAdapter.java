package fup.prototype.domain.api;

import android.support.annotation.Nullable;

import io.reactivex.annotations.NonNull;

/**
 * Empty implementation
 *
 * @param <T> the desired result object as generic
 */
public class ApiCallAdapter<T> implements OnApiCallListener<T> {

    @Override
    public void onLoadingStateChanged(@NonNull final LoadingState loadingState) {

    }

    @Override
    public void onApiCallDone(@Nullable final T t) {

    }

   @Override
    public void onApiCallError(@NonNull final RequestError requestError) {

    }
}
