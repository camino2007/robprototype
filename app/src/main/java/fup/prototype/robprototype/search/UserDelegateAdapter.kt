package fup.prototype.robprototype.search

import android.support.v7.widget.RecyclerView
import com.rxdroid.common.adapter.DelegateAdapter
import com.rxdroid.common.adapter.ItemViewType
import fup.prototype.robprototype.R

class UserDelegateAdapter : DelegateAdapter() {

    override fun getLayoutId(): Int {
        return R.layout.item_user
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ItemViewType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}