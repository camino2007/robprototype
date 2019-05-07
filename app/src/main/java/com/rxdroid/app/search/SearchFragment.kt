package com.rxdroid.app.search

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
import com.rxdroid.app.BR
import com.rxdroid.app.R
import com.rxdroid.app.databinding.FragmentSearchBinding
import com.rxdroid.app.details.DetailActivity
import com.rxdroid.app.view.base.fragments.BaseFragment
import com.rxdroid.app.view.base.fragments.applyErrorHandlingWithDialog
import com.rxdroid.app.view.base.fragments.applyErrorHandlingWithSnackBar
import com.rxdroid.app.view.base.fragments.hideKeyboard
import com.rxdroid.app.view.base.viewmodels.ViewState
import com.rxdroid.common.Consumable
import com.rxdroid.common.invokeIfNeeded
import com.rxdroid.repository.model.User
import io.reactivex.rxkotlin.addTo
import org.koin.android.viewmodel.ext.android.viewModel


class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by viewModel()

    private var userAdapter = UserAdapter()

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }

    override fun initBinding(binding: FragmentSearchBinding) {
        binding.setVariable(BR.viewModel, viewModel)
        setupUserAdapter()
        viewModel.getUserItems()
                .observe(viewLifecycleOwner, Observer { userAdapter.clearAndAddItems(it) })
        viewModel.getClickedUserItem()
                .observe(viewLifecycleOwner, Observer { onClick(it) })
        viewModel.getViewState().observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState == ViewState.LOADING) {
                hideKeyboard()
            }
        })
        viewModel.initialize()
        applyErrorHandlingWithSnackBar(viewModel)
    }

    private fun setupUserAdapter() {
        val recyclerView: RecyclerView? = getViewBinding().recyclerView
        recyclerView?.adapter = userAdapter
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }

    override fun addViewListener() {
        addSearchInputListener()
    }

    override fun getLayoutId() = R.layout.fragment_search

    override fun getKey() = SearchFragment::class.java.name.toString()

    private fun addSearchInputListener() {
        getViewBinding()
                .input.textChanges()
                .subscribe { input: CharSequence? -> viewModel.updateSearchInput(input.toString()) }
                .addTo(getBaseCompositeDisposable())
    }

    private fun onClick(consumable: Consumable<User>) {
        consumable.invokeIfNeeded { user ->
            context?.let {
                val intent = DetailActivity.createIntent(it, user)
                it.startActivity(intent)
            }
        }
    }

}
