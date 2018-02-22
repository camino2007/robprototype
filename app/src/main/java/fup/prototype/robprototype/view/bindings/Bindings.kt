package fup.prototype.robprototype.view.bindings

import android.databinding.BindingAdapter
import android.view.View

class Bindings {

    @BindingAdapter("visibleOrGone")
    fun setVisibleOrGone(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}