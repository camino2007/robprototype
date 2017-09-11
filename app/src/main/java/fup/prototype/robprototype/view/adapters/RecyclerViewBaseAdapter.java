package fup.prototype.robprototype.view.adapters;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public abstract class RecyclerViewBaseAdapter<T, V extends ViewDataBinding>
        extends RecyclerView.Adapter<DataBoundViewHolder<V>> {

    private List<T> items = new ArrayList<>();

    @Override
    public final DataBoundViewHolder<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        V binding = createBinding(parent);
        return new DataBoundViewHolder<>(binding);
    }

    @Override
    public final void onBindViewHolder(DataBoundViewHolder<V> holder, int position) {
        //noinspection ConstantConditions
        bind(holder.binding, items.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void replace(@NonNull final List<T> update) {
        if (!items.isEmpty()) {
            items.clear();
        }
        items.addAll(update);
        notifyDataSetChanged();
    }

    protected abstract void bind(V binding, T item);

    protected abstract V createBinding(ViewGroup parent);

}
