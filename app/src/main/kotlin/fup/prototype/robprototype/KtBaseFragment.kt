package fup.prototype.robprototype

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import dagger.android.AndroidInjection
import fup.prototype.robprototype.view.ViewProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class KtBaseFragment<B : ViewDataBinding, LVM : BaseViewModel> : Fragment(), ViewProvider<B, LVM> {

    private val compositeDisposable = CompositeDisposable()

    private var viewBinding: B? = null

    private var viewModel: LVM? = null

    override fun onAttach(context: Context?) {
        AndroidInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return viewBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel == null) {
            viewModel = createViewModel()
        }
        initBinding(viewBinding)
    }

    override fun onResume() {
        super.onResume()
        addViewListener()
        applyLiveDataObserver()
    }

    override fun onPause() {
        removeListener()
        super.onPause()
    }

    protected fun hideKeyboard() {
        val activity = activity
        if (context != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = activity.currentFocus
            if (view != null) {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    protected fun addRxDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun removeListener() {
        compositeDisposable.clear()
    }

    fun getViewBinding(): B? {
        return viewBinding
    }

    fun getViewModel(): LVM? {
        return viewModel
    }

    protected fun applyLiveDataObserver() {}

    abstract fun getKey(): String
}


