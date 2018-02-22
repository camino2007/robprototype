package fup.prototype.robprototype.view.base.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.rxdroid.api.error.RequestError
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel : ViewModel() {

    val viewState: MutableLiveData<ViewState> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val errorSubject: PublishSubject<RequestError> = PublishSubject.create()

    init {
        setViewState(ViewState.INIT)
    }

    fun setViewState(viewState: ViewState) {
        this.viewState.postValue(viewState)
    }

    fun getErrorSubject(): PublishSubject<RequestError> {
        return errorSubject
    }

    fun getCompositeDisposable(): CompositeDisposable {
        return compositeDisposable
    }

    fun changeLoadingState(isLoading: Boolean) {
        if (isLoading) {
            setViewState(ViewState.LOADING)
        }
    }

    fun handleErrorCase(requestError: RequestError) {
        setViewState(ViewState.DATA_ERROR)
        this.errorSubject.onNext(requestError)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}