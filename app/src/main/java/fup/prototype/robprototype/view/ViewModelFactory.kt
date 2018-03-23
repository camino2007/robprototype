package fup.prototype.robprototype.view

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.rxdroid.repository.repositories.detail.DetailsUiRepository
import com.rxdroid.repository.repositories.search.UserUiRepository
import fup.prototype.robprototype.details.DetailViewModel
import fup.prototype.robprototype.search.SearchViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(private val userUiRepo: UserUiRepository,
                                           private val detailsUiRepo: DetailsUiRepository)
    : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(userUiRepo) as (T)
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(detailsUiRepo) as (T)
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}