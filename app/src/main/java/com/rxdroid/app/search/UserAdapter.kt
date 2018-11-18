package com.rxdroid.app.search

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import com.rxdroid.app.view.base.adapters.LoadingDelegateAdapter
import com.rxdroid.common.AdapterConstants
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.common.adapter.ViewTypeDelegateAdapter

class UserAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ItemViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object : ItemViewType {
        override fun getItemViewType(): Int {
            return AdapterConstants.LOADING_ITEM
        }
    }

    init {
        delegateAdapters.put(AdapterConstants.LOADING_ITEM, LoadingDelegateAdapter())
        delegateAdapters.put(AdapterConstants.USER_ITEM, UserDelegateAdapter())
        items = ArrayList()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = delegateAdapters.get(viewType)
        viewHolder?.also {
            return it.onCreateViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = delegateAdapters.get(getItemViewType(position))
        viewHolder?.also { it -> it.onBindViewHolder(holder, this.items[position]) }
    }

    override fun getItemViewType(position: Int): Int {
        return this.items[position].getItemViewType()
    }

    private fun addItems(newItems: List<ItemViewType>?) {
        var initPosition = 0
        if (!items.isEmpty()) {
            initPosition = items.size - 1
            if (getItemViewType(initPosition) == AdapterConstants.LOADING_ITEM) {
                //remove loading item
                items.removeAt(initPosition)
                notifyItemRemoved(initPosition)
            }
        }
        if (newItems != null && !newItems.isEmpty()) {
            items.addAll(newItems)
            if (newItems.size > 1) {
                items.add(loadingItem)
            }
            notifyItemRangeChanged(initPosition, items.size)
        }
    }

    fun clearAndAddItems(users: List<ItemViewType>?) {
        items.clear()
        addItems(users)
    }

}
