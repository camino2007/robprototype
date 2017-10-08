package fup.prototype.robprototype.view.errorview;

import android.databinding.ObservableField;
import fup.prototype.robprototype.view.viewmodels.BaseViewModel;

public class ErrorViewModel extends BaseViewModel {

    public ObservableField<String> errorTitle = new ObservableField<>();
    public ObservableField<String> errorDescription = new ObservableField<>();

    private String title = "";
    private String description = "";

    public ErrorViewModel(String title, String description) {
        this.title = title;
        this.description = description;
        errorTitle.set(title);
        errorDescription.set(description);
    }

    @Override
    protected void injectDependencies() {

    }

    @Override
    public void loadOrShowData() {

    }
}
