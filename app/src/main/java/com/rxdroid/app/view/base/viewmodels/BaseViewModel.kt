package com.rxdroid.app.view.base.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rxdroid.api.error.RequestError
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

open class BaseViewModel : ViewModel() {

    private val viewState: MutableLiveData<ViewState> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val errorSubject: PublishSubject<RequestError> = PublishSubject.create()

    init {
        setViewState(ViewState.INIT)
    }

    fun getViewState() = viewState

    fun setViewState(viewState: ViewState) = this.viewState.postValue(viewState)

    fun getErrorSubject() = errorSubject

    fun getCompositeDisposable() = compositeDisposable

    fun showLoadingState() = setViewState(ViewState.LOADING)

    fun handleErrorCase(requestError: RequestError?) {
        requestError?.also {
            setViewState(ViewState.DATA_ERROR)
            this.errorSubject.onNext(it)
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}