package fup.prototype.robprototype

import android.arch.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay
import com.rxdroid.api.error.RequestError
import com.rxdroid.repository.UserUiRepository
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.Status
import com.rxdroid.repository.model.User
import fup.prototype.robprototype.view.base.adapters.ObserverAdapter
import fup.prototype.robprototype.view.base.viewmodels.ViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchViewModel(repository: UserUiRepository) : BaseViewModel() {


    private val MIN_LENGTH_SEARCH: Int = 3
    private val DEBOUNCE_TIME_OUT: Long = 800L


    private val userUiRepository: UserUiRepository = repository
    private val searchValue: MutableLiveData<String> = MutableLiveData()
    private val publishRelay: PublishRelay<String> = PublishRelay.create()
    private val items: MutableLiveData<List<User>> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()

    init {
        addRepositoryDisposable()
    }

    private fun addRepositoryDisposable() {
        publishRelay.debounce(DEBOUNCE_TIME_OUT, TimeUnit.MILLISECONDS)
                .skip(1)
                .distinctUntilChanged()
                .filter({ searchValue -> searchValue.length >= MIN_LENGTH_SEARCH })
                .switchMap({ searchValue -> Observable.concat(getLoadingObservable(), userUiRepository.loadBySearchValue(searchValue)) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(UserObserver())
    }

    private fun getLoadingObservable(): Observable<Resource<User>> {
        return Observable.just(Resource<User>(Status.LOADING, null, null))
    }

    fun getItems(): MutableLiveData<List<User>> {
        return items
    }

    fun updateSearchInput(search: String?) {
        if (search != null) {
            searchValue.postValue(search)
            publishRelay.accept(search)
        }
    }

    private fun handleSuccessCase(user: User?) {
        if (user != null) {
            setViewState(ViewState.DATA_LOADED)
            showUserData(user)
        } else {
            setViewState(ViewState.NO_DATA)
        }
    }

    private fun showUserData(user: User) {
        userName.postValue(user.name)
        val users: List<User> = List(1, { user })
        items.postValue(users)
    }

    inner class UserObserver : ObserverAdapter<Resource<User>>() {

        override fun onSubscribe(disposable: Disposable) {
            getCompositeDisposable().add(disposable)
        }

        override fun onNext(userResource: Resource<User>) {
            changeLoadingState(userResource.status == Status.LOADING)
            when (userResource.status) {
                Status.ERROR -> handleErrorCase(userResource.requestError!!)
                Status.SUCCESS -> {
                    // storeToDatabase(userResource.data)
                    handleSuccessCase(userResource.data)
                }
            }
        }

        override fun onError(throwable: Throwable) {
            handleErrorCase(RequestError.create(null, throwable))
        }
    }

}