package fup.prototype.robprototype.search

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.rxdroid.common.AdapterConstants
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.common.adapter.ViewTypeDelegateAdapter
import fup.prototype.robprototype.view.base.adapters.LoadingDelegateAdapter

class UserAdapterNew : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters.get(viewType).onCreateViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(position).onBindViewHolder(holder, this.items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return this.items[position].getItemViewType()
    }

    fun addUsers(users: List<ItemViewType>?) {
        var initPosition = 0
        if (!items.isEmpty()) {
            initPosition = items.size - 1
            items.removeAt(initPosition)
            notifyItemRemoved(initPosition)
        }
        if (!users?.isEmpty()) {
            items.addAll(users)
            items.add(loadingItem)
            notifyItemRangeInserted(initPosition, items.size)
        }
    }

    fun clearAndAddItems(users: List<ItemViewType>?) {
        items.clear()
        addUsers(users)
    }

}