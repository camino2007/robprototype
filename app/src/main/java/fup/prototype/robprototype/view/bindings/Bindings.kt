package fup.prototype.robprototype.view.bindings

import android.databinding.BindingAdapter
import android.view.View

object Bindings {

    @JvmStatic
    @BindingAdapter("visibleOrGone")
    fun setVisibleOrGone(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}