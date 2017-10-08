package fup.prototype.robprototype.view.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import fup.prototype.domain.api.RequestError;
import io.reactivex.annotations.NonNull;

public abstract class BaseViewModel extends BaseObservable {

    public ObservableField<RequestError> requestError = new ObservableField<>();
    public ObservableField<ViewState> viewState = new ObservableField<>(ViewState.ON_INIT);

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
        //viewState.set(ViewState.ON_LOADING == isLoading);
    }

    public void onDataError(@NonNull final RequestError requestError) {
        setViewState(ViewState.ON_DATA_ERROR);
        this.requestError.set(requestError);
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
