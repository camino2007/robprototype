package fup.prototype.robprototype.view.base.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.databinding.PropertyChangeRegistry;
import android.support.annotation.NonNull;

import com.rxdroid.api.error.RequestError;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

public abstract class BaseLiveDataViewModel extends ViewModel implements Observable {

    private transient PropertyChangeRegistry mCallbacks;

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

    public void clear() {
        compositeDisposable.clear();
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (mCallbacks == null) {
                mCallbacks = new PropertyChangeRegistry();
            }
        }
        mCallbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.remove(callback);
    }

}
