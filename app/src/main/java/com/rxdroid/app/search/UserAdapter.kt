package com.rxdroid.app.search

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.rxdroid.common.AdapterConstants
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.common.adapter.ViewTypeDelegateAdapter
import com.rxdroid.app.view.base.adapters.LoadingDelegateAdapter

class UserAdapter(private val userClickHandler: SearchFragment.UserClickHandler) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ItemViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object : ItemViewType {
        override fun getItemViewType(): Int {
            return AdapterConstants.LOADING_ITEM
        }
    }

    init {
        delegateAdapters.put(AdapterConstants.LOADING_ITEM, LoadingDelegateAdapter())
        delegateAdapters.put(AdapterConstants.USER_ITEM, UserDelegateAdapter(userClickHandler))
        items = ArrayList()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters.get(viewType).onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, this.items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return this.items[position].getItemViewType()
    }

    fun addItems(newItems: ArrayList<ItemViewType>) {
        var initPosition = 0
        if (!items.isEmpty()) {
            initPosition = items.size - 1
            if (getItemViewType(initPosition) == AdapterConstants.LOADING_ITEM) {
                //remove loading item
                items.removeAt(initPosition)
                notifyItemRemoved(initPosition)
            }
        }
        if (!newItems.isEmpty()) {
            items.addAll(newItems)
            if (newItems.size > 1) {
                items.add(loadingItem)
            }
            notifyItemRangeChanged(initPosition, items.size)
        }
    }

    fun clearAndAddItems(users: ArrayList<ItemViewType>) {
        items.clear()
        addItems(users)
    }

}