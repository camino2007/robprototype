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
import com.rxdroid.app.view.ViewProvider
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<B : ViewDataBinding> : Fragment(), ViewProvider<B> {

    private val compositeDisposable = CompositeDisposable()

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

    protected fun hideKeyboard() {
        activity?.also {
            val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = it.currentFocus
            if (view != null) {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    fun getViewBinding(): B = viewBinding

    fun getBaseCompositeDisposable() = compositeDisposable

}
