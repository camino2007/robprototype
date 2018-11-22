package com.rxdroid.app.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay
import com.rxdroid.api.error.RequestError
import com.rxdroid.app.view.ItemViewModelFactory
import com.rxdroid.app.view.base.viewmodels.BaseViewModel
import com.rxdroid.app.view.base.viewmodels.ViewState
import com.rxdroid.common.Consumable
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.Status
import com.rxdroid.repository.model.User
import com.rxdroid.repository.repositories.search.UserSearchRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SearchViewModel(private val repository: UserSearchRepository,
                      private val debounceScheduler: Scheduler) : BaseViewModel() {

    private object Constants {
        const val MIN_LENGTH_SEARCH: Int = 3
        const val DEBOUNCE_TIME_OUT: Long = 800L
    }

    private val publishRelay: PublishRelay<String> = PublishRelay.create()

    private val userItem = MutableLiveData<List<ItemViewType>>()
    fun getUserItems(): LiveData<List<ItemViewType>> = userItem

    private val clickedUserItem = MutableLiveData<Consumable<User>>()
    fun getClickedUserItem(): LiveData<Consumable<User>> = clickedUserItem

    /*  init {
          addRepositoryDisposable()
      }*/

    fun addRepositoryDisposable() {
        Timber.i("addRepositoryDisposable")
        publishRelay.debounce(Constants.DEBOUNCE_TIME_OUT, TimeUnit.MILLISECONDS, debounceScheduler)
                .skip(1)
                .distinctUntilChanged()
                .filter { searchValue -> searchValue.length >= Constants.MIN_LENGTH_SEARCH }
                .switchMap<Resource<User>> { searchValue -> Observable.concat(getLoadingState(), repository.searchForUser(searchValue)) }
                .map<Resource<ItemViewType>> { userResource: Resource<User> ->
                    val viewModel: ItemViewType? = ItemViewModelFactory.create(userResource.data, this::onUserItemClicked)
                    Resource(userResource.status, viewModel, userResource.requestError)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    when (it.status) {
                        Status.LOADING -> showLoadingState()
                        Status.ERROR -> handleErrorCase(it.requestError)
                        Status.SUCCESS -> handleSuccessCase(it.data)
                    }
                }, { e: Throwable? -> handleErrorCase(RequestError.create(e)) })
                .addTo(getCompositeDisposable())
    }

    private fun onUserItemClicked(user: User) {
        clickedUserItem.value = Consumable(user)
    }

    private fun getLoadingState(): Observable<Resource<User>> = Observable.just(Resource.loading())

    fun updateSearchInput(search: String?) {
        search?.let {
            publishRelay.accept(it)
        }
    }

    private fun handleSuccessCase(userItemViewModel: ItemViewType?) {
        if (userItemViewModel != null) {
            setViewState(ViewState.DATA_LOADED)
            val newItems: ArrayList<ItemViewType> = ArrayList(1)
            newItems.add(userItemViewModel)
            userItem.postValue(newItems)
        } else {
            setViewState(ViewState.NO_DATA)
        }
    }

}
