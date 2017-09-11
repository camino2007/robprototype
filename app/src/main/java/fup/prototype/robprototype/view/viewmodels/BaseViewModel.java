package fup.prototype.robprototype.view.viewmodels;

import android.databinding.BaseObservable;
import android.os.Parcelable;

public abstract class BaseViewModel<VM> extends BaseObservable implements Parcelable {

    BaseViewModel() {
        injectDependencies();
    }

    protected abstract void injectDependencies();

}
