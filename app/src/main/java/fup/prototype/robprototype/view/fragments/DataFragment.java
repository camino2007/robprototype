package fup.prototype.robprototype.view.fragments;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import fup.prototype.domain.api.RequestError;
import fup.prototype.robprototype.util.DialogUtils;
import fup.prototype.robprototype.view.viewmodels.BaseViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Has support to show an error dialog when an API error happens
 *
 * @param <B>
 * @param <VM>
 */
public abstract class DataFragment<B extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment<B, VM> {

    //TODO think, if it's a good idea to store RequestError in onSaveInstance
    private static final String KEY_REQUEST_ERROR = "keyRequestError";

    private AlertDialog errorDialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Disposable errorDisposable = getViewModel().getErrorSubject().subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<RequestError>() {
            @Override
            public void accept(@NonNull final RequestError requestError) throws Exception {
                errorDialog = createErrorDialog(requestError);
                DialogUtils.showIfPossible(errorDialog);
            }
        });
        addRxDisposable(errorDisposable);
    }

    @Override
    public void onResume() {
        super.onResume();
        DialogUtils.showIfPossible(errorDialog);
    }

    @Override
    public void onPause() {
        DialogUtils.dismiss(errorDialog);
        super.onPause();
    }

    protected abstract AlertDialog createErrorDialog(@NonNull final RequestError requestError);
}
