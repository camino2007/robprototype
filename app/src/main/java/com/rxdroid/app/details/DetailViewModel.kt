package com.rxdroid.app.details

import androidx.lifecycle.MutableLiveData
import com.rxdroid.api.error.RequestError
import com.rxdroid.app.view.ItemViewModelFactory
import com.rxdroid.app.view.base.viewmodels.BaseViewModel
import com.rxdroid.app.view.base.viewmodels.ViewState
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.repository.model.Repository
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.Status
import com.rxdroid.repository.model.User
import com.rxdroid.repository.repositories.detail.DetailsRepository
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class DetailViewModel(private val repository: DetailsRepository) : BaseViewModel() {

    val userName: MutableLiveData<String> = MutableLiveData()

    private val items: MutableLiveData<List<ItemViewType>> = MutableLiveData()
    fun getItems(): MutableLiveData<List<ItemViewType>> = items

    fun loadReposForUser(user: User) {
        repository
                .loadRepositoriesForUser(user)
                .startWith(getLoadingObservable())
                .compose(getViewModelTransformer())
                .compose(getRetryBehavior().observableTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { listViewTypeResource ->
                            evaluateResource(listViewTypeResource)
                        },
                        onError = { throwable ->
                            Timber.e(throwable)
                            handleErrorCase(RequestError.create(throwable))
                        }
                )
                .addTo(getCompositeDisposable())
    }

    private fun evaluateResource(resource: Resource<List<ItemViewType>>) {
        when (resource.status) {
            Status.LOADING -> showLoadingState()
            Status.ERROR -> handleErrorCase(resource.requestError)
            Status.SUCCESS -> handleSuccessCase(resource.data)
        }
    }

    private fun getLoadingObservable(): Observable<Resource<List<Repository>>> = Observable.just(Resource.loading())

    private fun getViewModelTransformer(): ObservableTransformer<Resource<List<Repository>>, Resource<List<ItemViewType>>> {
        return ObservableTransformer { observableResource ->
            observableResource.map { resourceList ->
                val repositories = resourceList.data
                val viewModels: List<ItemViewType> = ItemViewModelFactory.create(repositories)
                Resource(resourceList.status, viewModels, resourceList.requestError)
            }
        }
    }

    private fun handleSuccessCase(newItems: List<ItemViewType>?) {
        if (newItems != null) {
            setViewState(ViewState.DATA_LOADED)
            items.postValue(newItems)
        } else {
            setViewState(ViewState.NO_DATA)
        }
    }

}
