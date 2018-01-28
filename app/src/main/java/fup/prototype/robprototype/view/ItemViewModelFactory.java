package fup.prototype.robprototype.view;

import android.support.annotation.NonNull;
import com.rxdroid.repository.model.Repository;
import com.rxdroid.repository.model.User;
import fup.prototype.robprototype.details.RepoItemViewModel;
import fup.prototype.robprototype.search.UserItemViewModel;

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
