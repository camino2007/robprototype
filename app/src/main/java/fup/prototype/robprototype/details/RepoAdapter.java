/*
package fup.prototype.robprototype.details;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.rxdroid.repository.model.Repository;
import fup.prototype.robprototype.R;
import fup.prototype.robprototype.databinding.ItemRepositoryBinding;
import fup.prototype.robprototype.view.ItemViewModelFactoryOld;
import fup.prototype.robprototype.view.base.adapters.RecyclerViewBaseAdapter;

public class RepoAdapter extends RecyclerViewBaseAdapter<Repository, ItemRepositoryBinding> {

    @Override
    protected void bind(final ItemRepositoryBinding binding, final Repository item) {
        final RepoItemViewModel viewModel = ItemViewModelFactoryOld.create(item);
        binding.setViewModel(viewModel);
        binding.setHandler(new RepoItemHandler());
    }

    @Override
    protected ItemRepositoryBinding createBinding(final ViewGroup parent) {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_repository, parent, false);
    }
}
*/
