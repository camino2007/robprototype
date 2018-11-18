package com.rxdroid.app.details

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import com.rxdroid.common.AdapterConstants
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.common.adapter.ViewTypeDelegateAdapter

class RepositoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ItemViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    init {
        delegateAdapters.put(AdapterConstants.REPOSITORY_ITEM, RepositoryDelegateAdapter())
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

    fun clearAndAddItems(newItems: List<ItemViewType>?) {
        items.clear()
        addItems(newItems)
    }

    fun addItems(newItems: List<ItemViewType>?) {
        var initPosition = 0
        if (!items.isEmpty()) {
            initPosition = items.size
        }
        if (newItems!=null && !newItems.isEmpty()) {
            items.addAll(newItems)
            notifyItemRangeChanged(initPosition, items.size)
        }
    }

}
