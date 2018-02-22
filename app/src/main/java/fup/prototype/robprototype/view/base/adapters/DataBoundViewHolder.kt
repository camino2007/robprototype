package fup.prototype.robprototype.view.base.adapters

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View

class DataBoundViewHolder<T : ViewDataBinding>(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private var binding: T? = null

    constructor(itemView: View?, binding: T) : this(itemView) {
        this.binding = binding
    }

    fun getBinding(): T? {
        return binding
    }
}