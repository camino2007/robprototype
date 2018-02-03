package fup.prototype.robprototype.view.base.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.rxdroid.api.error.RequestError;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

public abstract class BaseLiveDataViewModel extends ViewModel {

    public ObservableField<ViewState> viewState = new ObservableField<>(ViewState.ON_INIT);
    public ObservableField<Boolean> isInProgress = new ObservableField<>(false);

    private PublishSubject<RequestError> errorSubject = PublishSubject.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void setViewState(@NonNull final ViewState viewState) {
        this.viewState.set(viewState);
    }

    public ViewState getViewState() {
        return this.viewState.get();
    }

    public PublishSubject<RequestError> getErrorSubject() {
        return errorSubject;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public void changeLoadingState(final boolean isLoading) {
        isInProgress.set(isLoading);
    }

    public void handleErrorCase(final RequestError requestError) {
        setViewState(ViewState.ON_DATA_ERROR);
        this.errorSubject.onNext(requestError);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
    }

}
