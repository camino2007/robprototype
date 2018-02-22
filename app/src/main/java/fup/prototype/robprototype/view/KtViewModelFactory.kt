package fup.prototype.robprototype.view

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.rxdroid.repository.GithubDetailsUiRepository
import com.rxdroid.repository.UserUiRepository
import fup.prototype.robprototype.search.SearchViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KtViewModelFactory @Inject constructor(private val userUiRepo: UserUiRepository,
                                             private val githubDetailsUiRepo: GithubDetailsUiRepository)
    : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(userUiRepo) as (T)
        }
        throw IllegalArgumentException("Unknown ViewModel class")
/*
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
    }*/
    }
}