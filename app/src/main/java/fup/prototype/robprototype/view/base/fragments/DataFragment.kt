package fup.prototype.robprototype.view.base.fragments

import android.databinding.ViewDataBinding
import android.support.v7.app.AlertDialog
import com.rxdroid.api.error.RequestError
import fup.prototype.robprototype.util.DialogUtils
import fup.prototype.robprototype.view.base.viewmodels.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable


abstract class DataFragment<B : ViewDataBinding, out LVM : BaseViewModel> : BaseFragment<B, LVM>() {

    private var errorDialog: AlertDialog? = null

    override fun onResume() {
        super.onResume()
        val errorDisposable: Disposable? = getViewModel()?.getErrorSubject()
                ?.subscribeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ requestError ->
                    errorDialog = createErrorDialog(requestError)
                    DialogUtils.showIfPossible(errorDialog)
                })
        if (errorDisposable != null) {
            addRxDisposable(errorDisposable)
        } else {
            throw IllegalArgumentException("IllegalArgumentException - ErrorDisposable is null")
        }
    }

    override fun onPause() {
        DialogUtils.dismiss(errorDialog)
        super.onPause()
    }

    protected abstract fun createErrorDialog(requestError: RequestError): AlertDialog
}

