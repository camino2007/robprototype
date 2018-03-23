package fup.prototype.robprototype.details

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
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
        return delegateAdapters.get(viewType).onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, this.items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return this.items[position].getItemViewType()
    }

    fun clearAndAddItems(newItems: ArrayList<ItemViewType>) {
        items.clear()
        addItems(newItems)
    }

    fun addItems(newItems: ArrayList<ItemViewType>) {
        var initPosition = 0
        if (!items.isEmpty()) {
            initPosition = items.size
        }
        if (!newItems.isEmpty()) {
            items.addAll(newItems)
            notifyItemRangeChanged(initPosition, items.size)
        }
    }

}