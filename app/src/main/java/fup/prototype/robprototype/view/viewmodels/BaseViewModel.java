package fup.prototype.robprototype.view.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import fup.prototype.domain.api.RequestError;
import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.PublishSubject;

public abstract class BaseViewModel extends BaseObservable {

    public ObservableField<ViewState> viewState = new ObservableField<>(ViewState.ON_INIT);
    public ObservableField<Boolean> isInProgress = new ObservableField<>(false);

    private PublishSubject<RequestError> errorSubject = PublishSubject.create();

    public BaseViewModel() {
        injectDependencies();
    }

    public void setViewState(@NonNull final ViewState viewState) {
        this.viewState.set(viewState);
    }

    public ViewState getViewState() {
        return this.viewState.get();
    }

    public void changeLoadingState(final boolean isLoading) {
        isInProgress.set(isLoading);
    }

    public void onDataError(@NonNull final RequestError requestError) {
        setViewState(ViewState.ON_DATA_ERROR);
        this.errorSubject.onNext(requestError);
    }

    public PublishSubject<RequestError> getErrorSubject() {
        return errorSubject;
    }

    public boolean isOnError() {
        return this.viewState.get() == ViewState.ON_DATA_ERROR;
    }

    public boolean isOnLoading() {
        return this.viewState.get() == ViewState.ON_LOADING;
    }

    protected abstract void injectDependencies();

    public abstract void loadOrShowData();
}
