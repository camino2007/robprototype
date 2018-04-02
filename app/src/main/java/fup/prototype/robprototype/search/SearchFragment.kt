package fup.prototype.robprototype.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
import com.rxdroid.api.error.RequestError
import com.rxdroid.common.ItemClickHandler
import fup.prototype.robprototype.BR
import fup.prototype.robprototype.R
import fup.prototype.robprototype.databinding.FragmentSearchBinding
import fup.prototype.robprototype.details.DetailActivity
import fup.prototype.robprototype.util.DialogUtils
import fup.prototype.robprototype.view.ViewModelFactory
import fup.prototype.robprototype.view.base.fragments.DataFragment
import fup.prototype.robprototype.view.base.viewmodels.ViewState
import io.reactivex.disposables.Disposable
import java.net.HttpURLConnection
import javax.inject.Inject


class SearchFragment : DataFragment<FragmentSearchBinding, SearchViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var userAdapter = UserAdapter(UserClickHandler())

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }

    override fun createViewModel(): SearchViewModel {
        return ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    override fun initBinding(binding: FragmentSearchBinding?) {
        binding?.setVariable(BR.viewModel, getViewModel())
        binding?.setLifecycleOwner(this)
        setupUserAdapter()
    }

    private fun setupUserAdapter() {
        val recyclerView: RecyclerView? = getViewBinding()?.recyclerView
        recyclerView?.adapter = userAdapter
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search
    }

    override fun getKey(): String {
        return SearchFragment.toString()
    }

    override fun applyLiveDataObserver() {
        addUserObserver()
        addKeyboardObserver()
    }

    private fun addKeyboardObserver() {
        getViewModel()?.viewState?.observe(this, Observer { viewState ->
            if (viewState == ViewState.LOADING) {
                hideKeyboard()
            }
        })
    }

    private fun addUserObserver() {
        getViewModel()?.getItems()?.observe(this, Observer { users ->
            kotlin.run {
                if (users != null && !users.isEmpty()) {
                    userAdapter.clearAndAddItems(users)
                }
            }
        })
    }

    override fun addViewListener() {
        addSearchInputListener()
    }

    private fun addSearchInputListener() {
        val disposable: Disposable? = getViewBinding()?.input
                ?.textChanges()
                ?.subscribe({ chars -> getViewModel()?.updateSearchInput(chars.toString()) })
        if (disposable != null) {
            addRxDisposable(disposable)
        }
    }

    override fun createErrorDialog(requestError: RequestError): AlertDialog {
        if (requestError.response != null && requestError.response!!.code() == HttpURLConnection.HTTP_NOT_FOUND) {
            return DialogUtils.createOkCancelDialog(context!!, "Möp", "User not found", "Ok", "F*ck it", null, null)
        }
        if (requestError.errorCode == RequestError.ERROR_CODE_NO_SEARCH_INPUT) {
            val errorText = "If you leave this field blank, sooner or later I'll load all users."
            return DialogUtils.createOkCancelDialog(context!!, "ToDo", errorText, "Ok", "F*ck it", null, null)
        }
        return DialogUtils.createOkCancelDialog(context!!, "Möp", "A wild error occurred", "Ok", "F*ck it", null, null)

    }

    inner class UserClickHandler : ItemClickHandler<UserItemViewModel> {

        override fun onClick(t: UserItemViewModel) {
            context?.let {
                val intent = DetailActivity.createIntent(context!!, t.getUser())
                context!!.startActivity(intent)
            }

        }

    }
}
