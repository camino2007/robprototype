package fup.prototype.robprototype.view.base.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.rxdroid.api.error.RequestError;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

public abstract class BaseLiveDataViewModel extends ViewModel {

    private static final String TAG = "BaseLiveDataViewModel";

    public MutableLiveData<ViewState> viewStateLiveData = new MutableLiveData<>();
    private PublishSubject<RequestError> errorSubject = PublishSubject.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BaseLiveDataViewModel() {
        setViewState(ViewState.INIT);
    }

    public void setViewState(@NonNull final ViewState viewState) {
        this.viewStateLiveData.postValue(viewState);
    }


    public MutableLiveData<ViewState> getViewState() {
        return viewStateLiveData;
    }

    public PublishSubject<RequestError> getErrorSubject() {
        return errorSubject;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public void changeLoadingState(final boolean isLoading) {
        Log.d(TAG, "changeLoadingState - isLoading: " + isLoading);
        if (isLoading) {
            viewStateLiveData.postValue(ViewState.LOADING);
        }
    }

    public void handleErrorCase(final RequestError requestError) {
        setViewState(ViewState.DATA_ERROR);
        this.errorSubject.onNext(requestError);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
    }

}
