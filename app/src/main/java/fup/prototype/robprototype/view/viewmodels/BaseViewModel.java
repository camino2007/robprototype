package fup.prototype.robprototype.view.viewmodels;

import android.databinding.BaseObservable;

public abstract class BaseViewModel extends BaseObservable {

    BaseViewModel() {
        injectDependencies();
    }

    protected abstract void injectDependencies();

}
