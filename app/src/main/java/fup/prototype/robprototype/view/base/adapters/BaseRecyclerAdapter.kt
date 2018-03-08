package fup.prototype.robprototype.view.base.adapters

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

abstract class BaseRecyclerAdapter<T, V : ViewDataBinding> : RecyclerView.Adapter<DataBoundViewHolder<V>>() {

    private var items: ArrayList<T> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataBoundViewHolder<V> {
        val binding: V = createBinding(parent)
        return DataBoundViewHolder(parent, binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<V>?, position: Int) {
        //noinspection ConstantConditions
        bind(holder?.getBinding(), items[position])
        holder?.getBinding()?.executePendingBindings()
    }

    fun replace(update: ArrayList<T>) {
        items.clear()
        items.addAll(update)
        notifyDataSetChanged()
    }

    abstract fun createBinding(parent: ViewGroup?): V

    abstract fun bind(binding: V?, item: T)

}