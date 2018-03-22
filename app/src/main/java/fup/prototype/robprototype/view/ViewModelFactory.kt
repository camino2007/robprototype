package fup.prototype.robprototype.view

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.rxdroid.repository.GithubDetailsUiRepository
import com.rxdroid.repository.UserUiRepository
import fup.prototype.robprototype.details.DetailViewModel
import fup.prototype.robprototype.search.SearchViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(private val userUiRepo: UserUiRepository,
                                           private val githubDetailsUiRepo: GithubDetailsUiRepository)
    : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(userUiRepo) as (T)
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(githubDetailsUiRepo) as (T)
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}