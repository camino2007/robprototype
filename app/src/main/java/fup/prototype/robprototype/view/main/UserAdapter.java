package fup.prototype.robprototype.view.main;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import fup.prototype.robprototype.R;
import fup.prototype.robprototype.databinding.ItemUserBinding;
import fup.prototype.robprototype.view.base.adapters.RecyclerViewBaseAdapter;
import fup.prototype.robprototype.view.main.model.User;
import fup.prototype.robprototype.view.main.viewmodels.ItemViewModelFactory;
import fup.prototype.robprototype.view.main.viewmodels.UserItemViewModel;

public class UserAdapter extends RecyclerViewBaseAdapter<User, ItemUserBinding> {

    @Override
    protected void bind(final ItemUserBinding binding, final User user) {
        final UserItemViewModel viewModel = ItemViewModelFactory.create(user);
        binding.setViewModel(viewModel);
    }

    @Override
    protected ItemUserBinding createBinding(ViewGroup parent) {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_user, parent, false);
    }
}
