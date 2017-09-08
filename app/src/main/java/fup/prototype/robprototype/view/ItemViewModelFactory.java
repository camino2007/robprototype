package fup.prototype.robprototype.view;

import android.support.annotation.NonNull;

import fup.prototype.robprototype.model.Repository;
import fup.prototype.robprototype.view.adapters.RepositoryItemViewModel;

public final class ItemViewModelFactory {

    public static RepositoryItemViewModel create(@NonNull final Repository repository){
        final RepositoryItemViewModel viewModel = new RepositoryItemViewModel();
        viewModel.repoName.set(repository.getName());
        return viewModel;
    }
}
