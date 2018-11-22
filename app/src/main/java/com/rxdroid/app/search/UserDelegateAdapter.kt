package com.rxdroid.app.search

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.rxdroid.app.BR
import com.rxdroid.app.R
import com.rxdroid.common.adapter.DelegateAdapter
import com.rxdroid.common.adapter.ItemViewType

class UserDelegateAdapter : DelegateAdapter() {

    override fun getLayoutId() = R.layout.item_user

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ItemViewType) {
        val binding: ViewDataBinding? = DataBindingUtil.getBinding(holder.itemView)
        binding?.let {
            val resultVar: Boolean = it.setVariable(BR.viewModel, item)
            if (!resultVar) {
                throw RuntimeException("Missing binding variable!")
            }
            it.executePendingBindings()
        }
    }

}
