package com.rxdroid.app.view.base.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rxdroid.api.RetryApiCallBehavior
import com.rxdroid.api.error.RequestError
import com.rxdroid.app.view.base.fragments.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject

open class BaseViewModel : ViewModel() {

    private val viewState: MutableLiveData<ViewState> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val errorSubject: PublishSubject<RequestError> = PublishSubject.create()

    private val retryBehavior = RetryApiCallBehavior()

    init {
        setViewState(ViewState.INIT)
    }

    fun getRetryBehavior() = retryBehavior

    fun getViewState() = viewState

    fun setViewState(viewState: ViewState) = this.viewState.postValue(viewState)

    fun getErrorSubject() = errorSubject

    fun getCompositeDisposable() = compositeDisposable

    fun showLoadingState() = setViewState(ViewState.LOADING)

    fun handleErrorCase(requestError: RequestError?) {
        requestError?.let {
            setViewState(ViewState.DATA_ERROR)
            this.errorSubject.onNext(it)
        }
    }

    fun applyRetryBehavior() {
        retryBehavior.getRetryErrorEvent()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { throwable ->
                    val requestError = RequestError.create(throwable)
                    handleErrorCase(requestError)
                }
                .addTo(getCompositeDisposable())
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}

fun BaseViewModel.retryLastCall() {
    getRetryBehavior().retryLastCall()
}
