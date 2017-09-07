package fup.prototype.robprototype.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fup.prototype.robprototype.BR;
import fup.prototype.robprototype.R;
import fup.prototype.robprototype.model.Repository;
import fup.prototype.robprototype.view.ViewModelFactory;

public class RepositoryAdapter extends RecyclerView.Adapter<RepoItemViewHolder> {

    private static final String TAG = "RepositoryAdapter";

    private List<Repository> repositories = new ArrayList<>();

    public RepositoryAdapter() {
        Log.d(TAG, "RepositoryAdapter: " + repositories.size());
    }

    @Override
    public RepoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repository, parent, false);
        return new RepoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepoItemViewHolder holder, int position) {
        final Repository repository = repositories.get(position);
        holder.getBinding().setVariable(BR.viewModel, ViewModelFactory.create(repository));
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public void replaceData(List<Repository> items) {
        Log.d(TAG, "replaceData");
        if (items != null && !items.isEmpty()) {
            Log.d(TAG, "replaceData: " + items.size());
            this.repositories.clear();
            this.repositories.addAll(items);
            notifyDataSetChanged();
        }
    }
}
