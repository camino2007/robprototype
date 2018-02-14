package fup.prototype.robprototype

import android.support.v7.app.AlertDialog
import com.rxdroid.api.error.RequestError
import fup.prototype.robprototype.databinding.FragmentSearchBinding

class KtSearchFragment : KtDataFragment<FragmentSearchBinding, SearchViewModel>() {

    companion object {
        fun newInstance(): KtSearchFragment = KtSearchFragment()
    }

    override fun createViewModel(): SearchViewModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initBinding(binding: FragmentSearchBinding?) {
        binding?.viewModel = getViewModel()
        binding?.setLifecycleOwner(this)
        setupUserAdapter()
    }

    private fun setupUserAdapter() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search
    }

    override fun getKey(): String {
        return KtSearchFragment.toString()
    }

    override fun addViewListener() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun createErrorDialog(requestError: RequestError): AlertDialog {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}