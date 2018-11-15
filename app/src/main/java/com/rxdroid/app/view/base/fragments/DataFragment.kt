package com.rxdroid.app.view.base.fragments

import androidx.databinding.ViewDataBinding
import androidx.appcompat.app.AlertDialog
import com.rxdroid.api.error.RequestError
import com.rxdroid.app.util.DialogUtils
import com.rxdroid.app.view.base.viewmodels.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers


abstract class DataFragment<B : ViewDataBinding> : BaseFragment<B>() {

    /*private var errorDialog: AlertDialog? = null

    override fun onResume() {
        super.onResume()
        val errorDisposable = getViewModel()?.getErrorSubject()
                ?.subscribeOn(AndroidSchedulers.mainThread())
                ?.subscribe { requestError ->
                    errorDialog = createErrorDialog(requestError)
                    DialogUtils.showIfPossible(errorDialog)
                }
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

    protected abstract fun createErrorDialog(requestError: RequestError): AlertDialog*/
}

