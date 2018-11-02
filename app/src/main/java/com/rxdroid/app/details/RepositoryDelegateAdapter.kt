package com.rxdroid.app.details

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.rxdroid.common.adapter.DelegateAdapter
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.app.BR
import com.rxdroid.app.R

class RepositoryDelegateAdapter : DelegateAdapter() {


    override fun getLayoutId(): Int {
        return R.layout.item_repository
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ItemViewType) {
        val binding: ViewDataBinding? = DataBindingUtil.getBinding(holder.itemView)
        binding.let {
            val resultVar: Boolean = binding!!.setVariable(BR.viewModel, item)
            if (!resultVar) {
                throw RuntimeException("Missing binding variable!")
            }
            binding.executePendingBindings()
        }
    }

}