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
import io.reactivex.BackpressureStrategy
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
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

    fun initialize() {
        publishRelay
                .debounce(Constants.DEBOUNCE_TIME_OUT, TimeUnit.MILLISECONDS, debounceScheduler)
                .skip(1)
                .distinctUntilChanged()
                .filter { searchValue -> searchValue.length >= Constants.MIN_LENGTH_SEARCH }
                .toFlowable(BackpressureStrategy.LATEST)
                .flatMap { searchValue ->
                    repository.searchForUser(searchValue)
                            .startWith(getLoadingState())
                            .toFlowable(BackpressureStrategy.LATEST)
                }
                .compose(getRetryBehavior().flowableTransformer())
                .compose(getViewModelTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { viewTypeResource ->
                            evaluateResource(viewTypeResource)
                        },
                        onError = { throwable ->
                            Timber.e(throwable)
                            handleErrorCase(RequestError.create(throwable))
                        }
                )
                .addTo(getCompositeDisposable())
    }

    private fun getViewModelTransformer(): FlowableTransformer<Resource<User>, Resource<ItemViewType>> {
        return FlowableTransformer { flowableUserResource ->
            flowableUserResource.map { userResource ->
                val viewModel = ItemViewModelFactory.create(userResource.data, this::performClick)
                Resource(userResource.status, viewModel, userResource.requestError)
            }
        }
    }

    private fun evaluateResource(resource: Resource<ItemViewType>) {
        when (resource.status) {
            Status.LOADING -> showLoadingState()
            Status.ERROR -> handleErrorCase(resource.requestError)
            Status.SUCCESS -> handleSuccessCase(resource.data)
        }
    }

    private fun performClick(action: ClickAction) = when (action) {
        is ClickAction.OnUser -> onUserItemClicked(action.user)
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

sealed class ClickAction {
    data class OnUser(val user: User) : ClickAction()
}
