package com.rxdroid.app.details

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.rxdroid.api.error.RequestError
import com.rxdroid.app.util.DialogUtils
import com.rxdroid.app.view.ViewModelFactory
import com.rxdroid.app.view.base.fragments.DataFragment
import com.rxdroid.repository.model.User
import com.rxdroid.app.BR
import com.rxdroid.app.R
import com.rxdroid.app.databinding.FragmentDetailsBinding

class DetailFragment : DataFragment<FragmentDetailsBinding, DetailViewModel>() {

    lateinit var viewModelFactory: ViewModelFactory

    private var repositoryAdapter = RepositoryAdapter()

    companion object {
        fun newInstance(user: User): DetailFragment {
            val bundle = Bundle()
            bundle.putSerializable(DetailActivity.DetailConstants.KEY_USER, user)
            val detailFragment = DetailFragment()
            detailFragment.arguments = bundle
            return detailFragment
        }
    }

    override fun initBinding(binding: FragmentDetailsBinding?) {
        binding?.setVariable(BR.viewModel, getViewModel())
        binding?.setLifecycleOwner(this)
        setupRepositoryAdapter()
    }

    private fun setupRepositoryAdapter() {
        val recyclerView: RecyclerView? = getViewBinding()?.recyclerView
        recyclerView?.adapter = repositoryAdapter
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }

    override fun createViewModel(): DetailViewModel? {
        return ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_details
    }

    override fun onResume() {
        super.onResume()
        val user: User = arguments?.getSerializable(DetailActivity.DetailConstants.KEY_USER) as User
        getViewModel()?.loadReposForUser(user)
    }

    override fun applyLiveDataObserver() {
        getViewModel()?.getItems()?.observe(this, Observer { items ->
            kotlin.run {
                if (items != null && !items.isEmpty()) {
                    repositoryAdapter.clearAndAddItems(items)
                }
            }
        })
    }

    override fun addViewListener() {
        //nothing
    }

    override fun createErrorDialog(requestError: RequestError): AlertDialog {
        return DialogUtils.createOkCancelDialog(context!!, "Möp", "A wild error occurred", "Ok", "F*ck it", null, null)

    }

    override fun getKey(): String {
        return DetailFragment.toString()
    }

}