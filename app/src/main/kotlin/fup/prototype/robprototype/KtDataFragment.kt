package fup.prototype.robprototype

import android.databinding.ViewDataBinding
import android.support.v7.app.AlertDialog
import com.rxdroid.api.error.RequestError
import fup.prototype.robprototype.util.DialogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable


abstract class KtDataFragment<B : ViewDataBinding, LVM : BaseViewModel> : KtBaseFragment<B, LVM>() {

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

