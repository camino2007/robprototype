package fup.prototype.robprototype.view.bindings;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import fup.prototype.robprototype.model.User;
import fup.prototype.robprototype.view.adapters.RepositoryAdapter;
import java.util.List;

public class Bindings {

    @BindingAdapter({"visibleOrGone"})
    public static void setVisibleOrGone(final View view, final boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter({"app:items"})
    public static void setItems(RecyclerView recyclerView, List<User> items) {
        RepositoryAdapter repositoryAdapter = (RepositoryAdapter) recyclerView.getAdapter();
        if (repositoryAdapter != null && items != null) {
            repositoryAdapter.replace(items);
        }
    }
}
