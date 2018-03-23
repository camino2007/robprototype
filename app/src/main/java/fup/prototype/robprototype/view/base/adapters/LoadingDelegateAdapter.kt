package fup.prototype.robprototype.view.base.adapters

import android.support.v7.widget.RecyclerView
import com.rxdroid.common.ItemClickHandler
import com.rxdroid.common.adapter.DelegateAdapter
import com.rxdroid.common.adapter.ItemViewType
import fup.prototype.robprototype.R

class LoadingDelegateAdapter : DelegateAdapter() {

    override fun getLayoutId(): Int {
        return R.layout.item_loading
    }

    override fun onBindViewHolder(clickHandler: ItemClickHandler<*>?, holder: RecyclerView.ViewHolder, item: ItemViewType) {
        //nothing to bind
    }

}