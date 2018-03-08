package fup.prototype.robprototype.search

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.jakewharton.rxrelay2.PublishRelay
import com.rxdroid.api.error.RequestError
import com.rxdroid.repository.UserUiRepository
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
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchViewModel(repository: UserUiRepository) : BaseViewModel() {

    private object Constants {
        const val TAG: String = "SearchViewModel"
        const val MIN_LENGTH_SEARCH: Int = 3
        const val DEBOUNCE_TIME_OUT: Long = 800L
    }

    private val userUiRepository = repository
    val searchValue: MutableLiveData<String> = MutableLiveData()
    private val publishRelay: PublishRelay<String> = PublishRelay.create()
    private val items: MutableLiveData<ArrayList<UserItemViewModel>> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()

    init {
        addRepositoryDisposable()
    }

    private fun addRepositoryDisposable() {
        publishRelay.debounce(Constants.DEBOUNCE_TIME_OUT, TimeUnit.MILLISECONDS)
                .skip(1)
                .distinctUntilChanged()
                .filter({ searchValue -> searchValue.length >= Constants.MIN_LENGTH_SEARCH })
                .switchMap<Resource<User>>({ searchValue -> Observable.concat(getLoadingObservable(), userUiRepository.loadBySearchValue(searchValue)) })
                .switchMap<UserItemViewModel>({ resource -> Observable.just(ItemViewModelFactory.create(resource.data)) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(UserObserver())
    }

    private fun getLoadingObservable(): Observable<Resource<User>> {
        return Observable.just(Resource<User>(Status.LOADING, null, null))
    }

    fun getItems(): MutableLiveData<ArrayList<UserItemViewModel>> {
        return items
    }

    fun updateSearchInput(search: String?) {
        search?.let {
            searchValue.postValue(search)
            publishRelay.accept(search)
        }
    }

    private fun handleSuccessCase(userItemViewModel: UserItemViewModel?) {
        if (userItemViewModel != null) {
            setViewState(ViewState.DATA_LOADED)
            showUserData(userItemViewModel)
        } else {
            setViewState(ViewState.NO_DATA)
        }
    }

    private fun showUserData(userItemViewModel: UserItemViewModel) {
        userName.postValue(userItemViewModel.loginName.value)
        val users: ArrayList<UserItemViewModel> = ArrayList(1)
        users.add(userItemViewModel)
        items.postValue(users)
    }

    private fun storeToDatabase(user: User) {
        userUiRepository.updateDatabase(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DatabaseWriteObserver())
    }

    inner class DatabaseWriteObserver : DisposableCompletableObserver() {

        override fun onComplete() {
            // In case you want to know, when db write succeeded
            Log.d(Constants.TAG, "DatabaseWriteObserver - onComplete")
        }

        override fun onError(e: Throwable) {
            //Database write transaction failed due of reasons ...
            Log.e(Constants.TAG, "DatabaseWriteObserver - onError: ", e)
        }

    }

    inner class UserObserver : ObserverAdapter<Resource<UserItemViewModel>>() {

        override fun onSubscribe(d: Disposable) {
            getCompositeDisposable().add(d)
        }

        override fun onNext(t: Resource<UserItemViewModel>) {
            changeLoadingState(t.status == Status.LOADING)
            when (t.status) {
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

}