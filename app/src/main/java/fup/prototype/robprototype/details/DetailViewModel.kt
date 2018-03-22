package fup.prototype.robprototype.details

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.rxdroid.api.error.RequestError
import com.rxdroid.common.adapter.ItemViewType
import com.rxdroid.repository.GithubDetailsUiRepository
import com.rxdroid.repository.model.Repository
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.Status
import com.rxdroid.repository.model.User
import fup.prototype.robprototype.view.ItemViewModelFactory
import fup.prototype.robprototype.view.base.adapters.ObserverAdapter
import fup.prototype.robprototype.view.base.viewmodels.BaseViewModel
import fup.prototype.robprototype.view.base.viewmodels.ViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class DetailViewModel(private val repository: GithubDetailsUiRepository) : BaseViewModel() {

    private object Constants {
        const val TAG: String = "DetailViewModel"
    }

    val userName: MutableLiveData<String> = MutableLiveData()

    private val items: MutableLiveData<ArrayList<ItemViewType>> = MutableLiveData()

    fun getItems(): MutableLiveData<ArrayList<ItemViewType>> {
        return items
    }

    fun loadReposForUser(user: User) {
        Observable.concat(getLoadingObservable(), repository.loadBySearchValue(user.login))
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
        Log.d(Constants.TAG, "handleSuccessCase - newItems?.size: " + newItems?.size)
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
                Status.SUCCESS -> {
                    // storeToDatabase(userResource.data)
                    handleSuccessCase(t.data)
                }
            }
        }

        override fun onError(e: Throwable) {
            handleErrorCase(RequestError.create(null, e))
        }
    }

    /*

    private void storeToDatabase(List<Repository> repositories) {
        final Completable completable = detailsUiRepository.updateDatabase(repositories, user.getId());
        completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DatabaseWriteObserver());
    }



    private class DatabaseWriteObserver extends DisposableCompletableObserver {

        @Override
        public void onError(final Throwable e) {
            //Database write transaction failed due of reasons ...
            Log.e(TAG, "DatabaseWriteObserver - onError: ", e);
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "DatabaseWriteObserver - onComplete: ");
        }
    }

*/
}


