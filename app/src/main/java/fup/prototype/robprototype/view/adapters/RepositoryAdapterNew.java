package fup.prototype.robprototype.view.adapters;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import fup.prototype.robprototype.R;
import fup.prototype.robprototype.databinding.ItemRepositoryBinding;
import fup.prototype.robprototype.model.Repository;
import fup.prototype.robprototype.view.ViewModelFactory;


public class RepositoryAdapterNew extends RecyclerViewBaseAdapter<Repository, ItemRepositoryBinding> {

    private final DataBindingComponent dataBindingComponent;

    public RepositoryAdapterNew(DataBindingComponent dataBindingComponent) {
        this.dataBindingComponent = dataBindingComponent;
    }

    @Override
    protected void bind(ItemRepositoryBinding binding, Repository item) {
        final RepositoryItemViewModel viewModel = ViewModelFactory.create(item);
        binding.setViewModel(viewModel);
    }

    @Override
    protected ItemRepositoryBinding createBinding(ViewGroup parent) {
        ItemRepositoryBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_repository,
                        parent, false, dataBindingComponent);
        return binding;
    }
}
