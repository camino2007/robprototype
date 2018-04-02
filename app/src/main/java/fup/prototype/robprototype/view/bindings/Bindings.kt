package fup.prototype.robprototype.view.bindings

import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

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

    @JvmStatic
    @BindingAdapter("glideAvatarUrl")
    fun setGlideAvatarUrl(view: ImageView, url: String) {
        Glide.with(view.context)
                .asDrawable()
                .load(url)
                .into(view)
    }

}