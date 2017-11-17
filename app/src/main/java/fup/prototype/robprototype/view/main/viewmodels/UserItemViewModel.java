package fup.prototype.robprototype.view.main.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

public class UserItemViewModel extends BaseObservable {

    public final ObservableField<String> userName = new ObservableField<>();
    public final ObservableField<String> repoCounter = new ObservableField<>();

}
