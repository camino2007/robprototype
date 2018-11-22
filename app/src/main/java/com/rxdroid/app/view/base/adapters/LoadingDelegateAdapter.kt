package com.rxdroid.app.view.base.adapters

import androidx.recyclerview.widget.RecyclerView
import com.rxdroid.app.R
import com.rxdroid.common.adapter.DelegateAdapter
import com.rxdroid.common.adapter.ItemViewType

class LoadingDelegateAdapter : DelegateAdapter() {

    override fun getLayoutId() = R.layout.item_loading

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ItemViewType) {
        //nothing to bind
    }

}