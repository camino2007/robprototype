package fup.prototype.robprototype.view.base.fragments;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.rxdroid.api.error.RequestError;

import fup.prototype.robprototype.util.DialogUtils;
import fup.prototype.robprototype.view.base.viewmodels.BaseLiveDataViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public abstract class DataFragment<B extends ViewDataBinding, LVM extends BaseLiveDataViewModel> extends BaseFragment<B, LVM> {

    private AlertDialog errorDialog;

    @Override
    public void onResume() {
        super.onResume();
        final Disposable errorDisposable = getViewModel().getErrorSubject()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RequestError>() {
                    @Override
                    public void accept(@NonNull final RequestError requestError) throws Exception {
                        errorDialog = createErrorDialog(requestError);
                        DialogUtils.showIfPossible(errorDialog);
                    }
                });
        addRxDisposable(errorDisposable);
        DialogUtils.showIfPossible(errorDialog);
    }

    @Override
    public void onPause() {
        DialogUtils.dismiss(errorDialog);
        super.onPause();
    }

    protected abstract AlertDialog createErrorDialog(@NonNull final RequestError requestError);

}
