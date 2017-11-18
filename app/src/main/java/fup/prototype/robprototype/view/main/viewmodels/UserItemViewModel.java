package fup.prototype.robprototype.view.main.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import fup.prototype.robprototype.view.main.model.User;

public class UserItemViewModel extends BaseObservable {

    public final ObservableField<String> loginName = new ObservableField<>();
    public final ObservableField<String> repoCounter = new ObservableField<>();

    private final User user;

    public UserItemViewModel(final User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
