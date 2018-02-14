package fup.prototype.robprototype.view;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.rxdroid.repository.GithubDetailsUiRepository;
import com.rxdroid.repository.UserUiRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import fup.prototype.robprototype.SearchViewModel;
import fup.prototype.robprototype.details.DetailViewModel;
import fup.prototype.robprototype.search.MainViewModel;

@Singleton
public class LiveDataViewModelFactory implements ViewModelProvider.Factory {

    private final UserUiRepository userUiRepository;
    private final GithubDetailsUiRepository githubDetailsUiRepository;

    @Inject
    public LiveDataViewModelFactory(@NonNull final UserUiRepository userUiRepository, @NonNull final GithubDetailsUiRepository githubDetailsUiRepository) {
        this.userUiRepository = userUiRepository;
        this.githubDetailsUiRepository = githubDetailsUiRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(userUiRepository);
        }
        if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(userUiRepository);
        }
        if (modelClass.isAssignableFrom(DetailViewModel.class)) {
            return (T) new DetailViewModel(githubDetailsUiRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
