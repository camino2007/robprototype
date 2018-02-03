package fup.prototype.robprototype.view;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.rxdroid.repository.GithubDetailsUiRepository;
import com.rxdroid.repository.UserUiRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import fup.prototype.robprototype.details.DetailViewModel;
import fup.prototype.robprototype.search.NewMainViewModel;

@Singleton
public class LiveDataViewModelFactory implements ViewModelProvider.Factory {

    private final UserUiRepository userUiRepository;
    private final GithubDetailsUiRepository githubDetailsUiRepository;

    @Inject
    public LiveDataViewModelFactory(@NonNull final UserUiRepository userUiRepository, @NonNull final GithubDetailsUiRepository githubDetailsUiRepository) {
        this.userUiRepository = userUiRepository;
        this.githubDetailsUiRepository = githubDetailsUiRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewMainViewModel.class)) {
            return (T) new NewMainViewModel(userUiRepository);
        }
        if (modelClass.isAssignableFrom(DetailViewModel.class)) {
            return (T) new DetailViewModel(githubDetailsUiRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
