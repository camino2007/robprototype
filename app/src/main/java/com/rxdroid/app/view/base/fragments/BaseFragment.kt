package com.rxdroid.app.view.base.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.rxdroid.api.error.RequestError
import com.rxdroid.app.util.getErrorDialog
import com.rxdroid.app.view.ViewProvider
import com.rxdroid.app.view.base.viewmodels.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

abstract class BaseFragment<B : ViewDataBinding> : Fragment(), ViewProvider<B> {

    val compositeDisposable = CompositeDisposable()

    private lateinit var viewBinding: B

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initBinding(viewBinding)
    }

    override fun onResume() {
        super.onResume()
        addViewListener()
    }

    override fun onPause() {
        removeViewListener()
        super.onPause()
    }

    private fun removeViewListener() {
        compositeDisposable.clear()
    }

    fun getViewBinding(): B = viewBinding

    fun getBaseCompositeDisposable() = compositeDisposable

}

fun BaseFragment<*>.applyErrorHandling(viewModel: BaseViewModel) {
    viewModel.getErrorSubject()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { requestError: RequestError ->
                showErrorDialog(requestError)
            }
            .addTo(compositeDisposable)
}

fun BaseFragment<*>.showErrorDialog(requestError: RequestError) {
    context?.let {
        val errorDialog = getErrorDialog(it, requestError)
        errorDialog.show()
    }
}

fun BaseFragment<*>.hideKeyboard(){
    activity?.let { fragmentActivity ->
        val imm = fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = fragmentActivity.currentFocus
        view?.also {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
