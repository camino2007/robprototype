package com.rxdroid.app.details

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rxdroid.app.BR
import com.rxdroid.app.R
import com.rxdroid.app.databinding.FragmentDetailsBinding
import com.rxdroid.app.view.base.fragments.DataFragment
import com.rxdroid.repository.model.User
import org.koin.android.viewmodel.ext.android.viewModel

class DetailFragment : DataFragment<FragmentDetailsBinding>() {

    private val viewModel: DetailViewModel by viewModel()

    private var repositoryAdapter = RepositoryAdapter()

    companion object {
        fun newInstance(user: User): DetailFragment {
            val bundle = Bundle()
            bundle.putParcelable(DetailActivity.DetailConstants.KEY_USER, user)
            val detailFragment = DetailFragment()
            detailFragment.arguments = bundle
            return detailFragment
        }
    }

    override fun initBinding(binding: FragmentDetailsBinding) {
        binding.setVariable(BR.viewModel, viewModel)
        binding.setLifecycleOwner(viewLifecycleOwner)
        setupRepositoryAdapter()
        viewModel.getItems()
                .observe(viewLifecycleOwner, Observer { it -> repositoryAdapter.clearAndAddItems(it) })
    }

    private fun setupRepositoryAdapter() {
        val recyclerView: RecyclerView? = getViewBinding().recyclerView
        recyclerView?.adapter = repositoryAdapter
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_details
    }

    override fun onResume() {
        super.onResume()
        val user: User? = arguments?.getParcelable(DetailActivity.DetailConstants.KEY_USER)
        user?.also {
            viewModel.loadReposForUser(it)
        }
    }

    override fun addViewListener() {
        //nothing
    }

    override fun getKey(): String {
        return DetailFragment.toString()
    }

}
