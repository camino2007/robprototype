package fup.prototype.robprototype.view.bindings;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.rxdroid.repository.model.Repository;
import com.rxdroid.repository.model.User;
import fup.prototype.robprototype.view.details.RepoAdapter;
import fup.prototype.robprototype.view.main.UserAdapter;
import java.util.List;

public class Bindings {

    @BindingAdapter({"visibleOrGone"})
    public static void setVisibleOrGone(final View view, final boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter({"app:items"})
    public static void setUsers(RecyclerView recyclerView, List<User> items) {
        UserAdapter userAdapter = (UserAdapter) recyclerView.getAdapter();
        if (userAdapter != null && items != null) {
            userAdapter.replace(items);
        }
    }

    @BindingAdapter({"app:items"})
    public static void setRepos(RecyclerView recyclerView, List<Repository> items) {
        RepoAdapter repoAdapter = (RepoAdapter) recyclerView.getAdapter();
        if (repoAdapter != null && items != null) {
            repoAdapter.replace(items);
        }
    }
}
