package fup.prototype.robprototype.view.bindings;

import android.databinding.BindingAdapter;
import android.view.View;

public class Bindings {

    @BindingAdapter({"visibleOrGone"})
    public static void setVisibleOrGone(final View view, final boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

}
