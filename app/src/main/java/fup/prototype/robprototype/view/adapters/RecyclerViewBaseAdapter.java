package fup.prototype.robprototype.view.adapters;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public abstract class RecyclerViewBaseAdapter<T, V extends ViewDataBinding>
        extends RecyclerView.Adapter<DataBoundViewHolder<V>> {

    @Nullable
    private List<T> items;

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

    public void replace(List<T> update) {
        if (items != null) {
            items.clear();
        }
        items = update;
        notifyDataSetChanged();
    }

    protected abstract void bind(V binding, T item);

    protected abstract V createBinding(ViewGroup parent);

}
