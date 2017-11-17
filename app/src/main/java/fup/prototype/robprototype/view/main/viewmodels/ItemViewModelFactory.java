package fup.prototype.robprototype.view.main.viewmodels;

import android.support.annotation.NonNull;
import fup.prototype.robprototype.view.main.model.User;

public final class ItemViewModelFactory {

    public static UserItemViewModel create(@NonNull final User user) {
        final UserItemViewModel viewModel = new UserItemViewModel();
        viewModel.userName.set(user.getLogin());
        viewModel.repoCounter.set(String.valueOf(user.getPublicRepoCount()));
        return viewModel;
    }
}
