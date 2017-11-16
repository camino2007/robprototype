package fup.prototype.robprototype.view;

import android.support.annotation.NonNull;
import fup.prototype.robprototype.model.User;
import fup.prototype.robprototype.view.adapters.UserItemViewModel;

public final class ItemViewModelFactory {

    public static UserItemViewModel create(@NonNull final User user) {
        final UserItemViewModel viewModel = new UserItemViewModel();
        viewModel.userName.set(user.getLogin());
        viewModel.repoCounter.set(String.valueOf(user.getPublicRepoCount()));
        return viewModel;
    }
}
