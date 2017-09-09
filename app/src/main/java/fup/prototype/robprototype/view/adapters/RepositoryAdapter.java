package fup.prototype.robprototype.view.adapters;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import fup.prototype.robprototype.R;
import fup.prototype.robprototype.databinding.ItemRepositoryBinding;
import fup.prototype.robprototype.model.Repository;
import fup.prototype.robprototype.view.ItemViewModelFactory;


public class RepositoryAdapter extends RecyclerViewBaseAdapter<Repository, ItemRepositoryBinding> {

    @Override
    protected void bind(ItemRepositoryBinding binding, Repository item) {
        final RepositoryItemViewModel viewModel = ItemViewModelFactory.create(item);
        binding.setViewModel(viewModel);
    }

    @Override
    protected ItemRepositoryBinding createBinding(ViewGroup parent) {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_repository, parent, false);
    }
}
