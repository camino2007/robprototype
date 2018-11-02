package com.rxdroid.app.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay
import com.rxdroid.api.error.RequestError
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.Status
import com.rxdroid.repository.model.User
import com.rxdroid.repository.repositories.search.UserUiRepository
import com.rxdroid.app.view.ItemViewModelFactory
import com.rxdroid.app.view.base.adapters.ObserverAdapter
import com.rxdroid.app.view.base.viewmodels.BaseViewModel
import com.rxdroid.app.view.base.viewmodels.ViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchViewModel(private val repository: UserUiRepository) : BaseViewModel() {

    private object Constants {
        const val MIN_LENGTH_SEARCH: Int = 3
        const val DEBOUNCE_TIME_OUT: Long = 800L
    }

    val searchValue: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()

    private val items: MutableLiveData<ArrayList<ItemViewType>> = MutableLiveData()
    private val publishRelay: PublishRelay<String> = PublishRelay.create()

    private val resultResource = MutableLiveData<Resource<ItemViewType>>()
    fun getResultResource(): LiveData<Resource<ItemViewType>> = resultResource

    init {
        addRepositoryDisposable()
    }

    private fun addRepositoryDisposable() {
        publishRelay.debounce(Constants.DEBOUNCE_TIME_OUT, TimeUnit.MILLISECONDS)
                .skip(1)
                .distinctUntilChanged()
                .filter { searchValue -> searchValue.length >= Constants.MIN_LENGTH_SEARCH }
                .switchMap<Resource<User>> { searchValue -> Observable.concat(getLoadingObservable(), repository.searchForUser(searchValue)) }
                .switchMap<Resource<ItemViewType>> { resource ->
                    run {
                        val viewModel: ItemViewType? = ItemViewModelFactory.create(resource.data)
                        val modelResource: Resource<ItemViewType> = Resource(resource.status, viewModel, resource.requestError)
                        return@run Observable.just(modelResource)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // .addTo(getCompositeDisposable())
                .subscribe(UserObserver())
    }

    private fun getLoadingObservable(): Observable<Resource<User>> {
        return Observable.just(Resource.loading())
    }

    fun getItems(): MutableLiveData<ArrayList<ItemViewType>> {
        return items
    }

    fun updateSearchInput(search: String?) {
        search?.let {
            searchValue.postValue(search)
            publishRelay.accept(search)
        }
    }

    private fun handleSuccessCase(userItemViewModel: ItemViewType?) {
        if (userItemViewModel != null) {
            setViewState(ViewState.DATA_LOADED)
            showUserData(userItemViewModel)
        } else {
            setViewState(ViewState.NO_DATA)
        }
    }

    private fun showUserData(userItemViewModel: ItemViewType) {
        //TODO move to item view model
        //userName.postValue(userItemViewModel.loginName.value)
        val newItems: ArrayList<ItemViewType> = ArrayList(1)
        newItems.add(userItemViewModel)
        items.postValue(newItems)
    }

    inner class UserObserver : ObserverAdapter<Resource<ItemViewType>>() {

        override fun onSubscribe(d: Disposable) {
            getCompositeDisposable().add(d)
        }

        override fun onNext(t: Resource<ItemViewType>) {
            when (t.status) {
                Status.LOADING -> showLoadingState()
                Status.ERROR -> handleErrorCase(t.requestError!!)
                Status.SUCCESS -> handleSuccessCase(t.data)
            }
        }

        override fun onError(e: Throwable) {
            handleErrorCase(RequestError.create(e))
        }
    }

}