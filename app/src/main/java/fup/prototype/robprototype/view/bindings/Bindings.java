package fup.prototype.robprototype.view.bindings;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import fup.prototype.robprototype.view.main.UserAdapter;
import fup.prototype.robprototype.view.main.model.User;
import java.util.List;

public class Bindings {

    @BindingAdapter({"visibleOrGone"})
    public static void setVisibleOrGone(final View view, final boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter({"app:items"})
    public static void setItems(RecyclerView recyclerView, List<User> items) {
        UserAdapter userAdapter = (UserAdapter) recyclerView.getAdapter();
        if (userAdapter != null && items != null) {
            userAdapter.replace(items);
        }
    }
}
