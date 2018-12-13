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
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers


class DetailViewModel(private val repository: DetailsRepository) : BaseViewModel() {

    val userName: MutableLiveData<String> = MutableLiveData()

    private val items: MutableLiveData<List<ItemViewType>> = MutableLiveData()
    fun getItems(): MutableLiveData<List<ItemViewType>> = items


    fun loadReposForUser(user: User) {
        Flowable.concat(getLoadingObservable(), repository.loadRepositoriesForUser(user))
                .flatMap<Resource<List<ItemViewType>>> { resource: Resource<List<Repository>> ->
                    val repositories = resource.data
                    val viewModels: List<ItemViewType> = ItemViewModelFactory.create(repositories)
                    val modelResource: Resource<List<ItemViewType>> = Resource(resource.status, viewModels, resource.requestError)
                    Flowable.just(modelResource)
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

    private fun getLoadingObservable(): Flowable<Resource<List<Repository>>> = Flowable.just(Resource.loading())

    private fun handleSuccessCase(newItems: List<ItemViewType>?) {
        if (newItems != null) {
            setViewState(ViewState.DATA_LOADED)
            items.postValue(newItems)
        } else {
            setViewState(ViewState.NO_DATA)
        }
    }

}
