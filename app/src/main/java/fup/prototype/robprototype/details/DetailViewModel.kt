package fup.prototype.robprototype.details

import android.arch.lifecycle.MutableLiveData
import com.rxdroid.api.error.RequestError
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.repository.model.Repository
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.Status
import com.rxdroid.repository.model.User
import com.rxdroid.repository.repositories.detail.DetailsUiRepository
import fup.prototype.robprototype.view.ItemViewModelFactory
import fup.prototype.robprototype.view.base.adapters.ObserverAdapter
import fup.prototype.robprototype.view.base.viewmodels.BaseViewModel
import fup.prototype.robprototype.view.base.viewmodels.ViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class DetailViewModel(private val repository: DetailsUiRepository) : BaseViewModel() {

    val userName: MutableLiveData<String> = MutableLiveData()

    private val items: MutableLiveData<ArrayList<ItemViewType>> = MutableLiveData()

    fun getItems(): MutableLiveData<ArrayList<ItemViewType>> {
        return items
    }

    fun loadReposForUser(user: User) {
        Observable.concat(getLoadingObservable(), repository.loadReposForUser(user))
                .switchMap<Resource<ArrayList<ItemViewType>>>({ resource: Resource<List<Repository>> ->
                    kotlin.run {
                        val repositories = resource.data
                        val viewModels: ArrayList<ItemViewType> = ItemViewModelFactory.create(repositories)
                        val modelResource: Resource<ArrayList<ItemViewType>> = Resource(resource.status, viewModels, resource.requestError)
                        return@run Observable.just(modelResource)
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(RepositoryObserver())
    }

    private fun getLoadingObservable(): Observable<Resource<List<Repository>>> {
        return Observable.just(Resource.loading())
    }

    private fun handleSuccessCase(newItems: ArrayList<ItemViewType>?) {
        if (newItems != null) {
            setViewState(ViewState.DATA_LOADED)
            items.postValue(newItems)
        } else {
            setViewState(ViewState.NO_DATA)
        }
    }

    inner class RepositoryObserver : ObserverAdapter<Resource<ArrayList<ItemViewType>>>() {

        override fun onSubscribe(d: Disposable) {
            getCompositeDisposable().add(d)
        }

        override fun onNext(t: Resource<ArrayList<ItemViewType>>) {
            when (t.status) {
                Status.LOADING -> showLoadingState()
                Status.ERROR -> handleErrorCase(t.requestError!!)
                Status.SUCCESS -> handleSuccessCase(t.data)
            }
        }

        override fun onError(e: Throwable) {
            handleErrorCase(RequestError.create(null, e))
        }
    }

}


