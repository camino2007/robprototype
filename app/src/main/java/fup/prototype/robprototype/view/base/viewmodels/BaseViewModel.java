package fup.prototype.robprototype.view.base.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

import com.rxdroid.api.error.RequestError;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

public abstract class BaseViewModel extends BaseObservable {

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

    public void changeLoadingState(final boolean isLoading) {
        isInProgress.set(isLoading);
    }

    public void handleErrorCase(@NonNull final RequestError requestError) {
        setViewState(ViewState.ON_DATA_ERROR);
        this.errorSubject.onNext(requestError);
    }

    public PublishSubject<RequestError> getErrorSubject() {
        return errorSubject;
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public void clear() {
        compositeDisposable.clear();
    }

    public boolean isOnError() {
        return this.viewState.get() == ViewState.ON_DATA_ERROR;
    }

    public boolean isOnLoading() {
        return this.viewState.get() == ViewState.ON_LOADING;
    }

    public abstract void loadOrShowData();
}
