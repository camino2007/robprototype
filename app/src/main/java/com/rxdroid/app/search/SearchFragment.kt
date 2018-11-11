package com.rxdroid.app.search

import android.arch.lifecycle.Observer
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
import com.rxdroid.api.error.RequestError
import com.rxdroid.app.BR
import com.rxdroid.app.R
import com.rxdroid.app.databinding.FragmentSearchBinding
import com.rxdroid.app.details.DetailActivity
import com.rxdroid.app.util.DialogUtils
import com.rxdroid.app.view.base.fragments.DataFragment
import com.rxdroid.app.view.base.viewmodels.ViewState
import com.rxdroid.common.ItemClickHandler
import com.rxdroid.repository.model.User
import io.reactivex.disposables.Disposable
import org.koin.android.viewmodel.ext.android.viewModel


class SearchFragment : DataFragment<FragmentSearchBinding, SearchViewModel>() {

    private val viewModel: SearchViewModel by viewModel()

    private var userAdapter = UserAdapter()

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }

    override fun createViewModel(): SearchViewModel = viewModel

    override fun initBinding(binding: FragmentSearchBinding?) {
        binding?.setVariable(BR.viewModel, getViewModel())
        binding?.setLifecycleOwner(this)
        setupUserAdapter()
        viewModel.getUserItems().observe(this, Observer { it -> userAdapter.clearAndAddItems(it) })
        viewModel.getClickedUserItem().observe(this, Observer { it -> onClick(it) })
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
        addKeyboardObserver()
    }

    private fun addKeyboardObserver() {
        viewModel.viewState.observe(this, Observer { viewState ->
            if (viewState == ViewState.LOADING) {
                hideKeyboard()
            }
        })
    }

    override fun addViewListener() {
        addSearchInputListener()
    }

    private fun addSearchInputListener() {
        val disposable: Disposable? = getViewBinding()?.input
                ?.textChanges()
                ?.subscribe { chars -> getViewModel()?.updateSearchInput(chars.toString()) }
        if (disposable != null) {
            addRxDisposable(disposable)
        }
    }

    private fun onClick(user: User?) {
        if (user != null) {
            context?.let {
                val intent = DetailActivity.createIntent(context!!, user)
                context!!.startActivity(intent)
            }
        }

    }

    override fun createErrorDialog(requestError: RequestError): AlertDialog {
/*        when(requestError){

        }


        if (requestError.response != null && requestError.response!!.code() == HttpURLConnection.HTTP_NOT_FOUND) {
            return DialogUtils.createOkCancelDialog(context!!, "Möp", "User not found", "Ok", "F*ck it", null, null)
        }
        if (requestError.errorCode == RequestError.ERROR_CODE_NO_SEARCH_INPUT) {
            val errorText = "If you leave this field blank, sooner or later I'll load all users."
            return DialogUtils.createOkCancelDialog(context!!, "ToDo", errorText, "Ok", "F*ck it", null, null)
        }*/
        return DialogUtils.createOkCancelDialog(context!!, "Möp", "A wild error occurred", "Ok", "F*ck it", null, null)

    }

}
