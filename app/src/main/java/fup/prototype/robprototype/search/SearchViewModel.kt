package fup.prototype.robprototype.search

import android.arch.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay
import com.rxdroid.api.error.RequestError
import com.rxdroid.repository.model.Resource
import com.rxdroid.repository.model.Status
import com.rxdroid.repository.model.User
import fup.prototype.robprototype.view.base.adapters.ObserverAdapter
import fup.prototype.robprototype.view.base.viewmodels.BaseViewModel
import fup.prototype.robprototype.view.base.viewmodels.ViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchViewModel(repository: UserUiRepositoryOld) : BaseViewModel() {

    private val MIN_LENGTH_SEARCH: Int = 3
    private val DEBOUNCE_TIME_OUT: Long = 800L

    private val userUiRepository: UserUiRepositoryOld = repository
    val searchValue: MutableLiveData<String> = MutableLiveData()
    private val publishRelay: PublishRelay<String> = PublishRelay.create()
    private val items: MutableLiveData<MutableList<User>> = MutableLiveData()
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

    fun getItems(): MutableLiveData<MutableList<User>> {
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
        val users: MutableList<User> = MutableList(1, { user })
        items.postValue(users)
    }

/*    private void storeToDatabase(final User user) {
        final Completable completable = userUiRepository.updateDatabase(user);
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
            // In case you want to know, when db write succeeded
            Log.d(TAG, "DatabaseWriteObserver - onComplete");
        }
    }*/

    inner class UserObserver : ObserverAdapter<Resource<User>>() {

        override fun onSubscribe(d: Disposable) {
            getCompositeDisposable().add(d)
        }

        override fun onNext(t: Resource<User>) {
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