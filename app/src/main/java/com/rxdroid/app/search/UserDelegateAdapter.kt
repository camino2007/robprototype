package com.rxdroid.app.search

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import com.rxdroid.common.adapter.DelegateAdapter
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.app.BR
import com.rxdroid.app.R

class UserDelegateAdapter(private val userClickHandler: SearchFragment.UserClickHandler) : DelegateAdapter() {


    override fun getLayoutId(): Int {
        return R.layout.item_user
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ItemViewType) {
        val binding: ViewDataBinding? = DataBindingUtil.getBinding(holder.itemView)
        binding?.let {
            val resultVar: Boolean = binding.setVariable(BR.viewModel, item)
          /*  if (!resultVar) {
                throw RuntimeException("Missing binding variable!")
            }*/
          /*  val handlerResult = binding.setVariable(BR.handler, userClickHandler)
            if (!handlerResult) {
                throw RuntimeException("Missing handler variable!")
            }*/

            binding.executePendingBindings()
        }
    }


}