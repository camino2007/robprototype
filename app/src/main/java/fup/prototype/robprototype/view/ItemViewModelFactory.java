package fup.prototype.robprototype.view;

import android.support.annotation.NonNull;
import fup.prototype.robprototype.view.details.RepoItemViewModel;
import fup.prototype.robprototype.view.main.model.Repository;
import fup.prototype.robprototype.view.main.model.User;
import fup.prototype.robprototype.view.main.viewmodels.UserItemViewModel;

public final class ItemViewModelFactory {

    public static UserItemViewModel create(@NonNull final User user) {
        final UserItemViewModel viewModel = new UserItemViewModel(user);
        viewModel.loginName.set(user.getLogin());
        viewModel.repoCounter.set(String.valueOf(user.getPublicRepoCount()));
        return viewModel;
    }

    public static RepoItemViewModel create(@NonNull final Repository repository) {
        final RepoItemViewModel viewModel = new RepoItemViewModel(repository);
        viewModel.repoName.set(repository.getName());
        return viewModel;
    }
}
