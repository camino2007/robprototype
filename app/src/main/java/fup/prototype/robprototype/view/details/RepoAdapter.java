package fup.prototype.robprototype.view.details;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import fup.prototype.robprototype.R;
import fup.prototype.robprototype.databinding.ItemRepositoryBinding;
import fup.prototype.robprototype.view.ItemViewModelFactory;
import fup.prototype.robprototype.view.base.adapters.RecyclerViewBaseAdapter;
import fup.prototype.robprototype.view.main.model.Repository;

public class RepoAdapter extends RecyclerViewBaseAdapter<Repository, ItemRepositoryBinding> {

    @Override
    protected void bind(final ItemRepositoryBinding binding, final Repository item) {
        final RepoItemViewModel viewModel = ItemViewModelFactory.create(item);
        binding.setViewModel(viewModel);
        binding.setHandler(new RepoItemHandler());
    }

    @Override
    protected ItemRepositoryBinding createBinding(final ViewGroup parent) {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_repository, parent, false);
    }
}
