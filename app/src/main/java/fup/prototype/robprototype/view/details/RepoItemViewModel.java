package fup.prototype.robprototype.view.details;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import com.rxdroid.repository.model.Repository;

public class RepoItemViewModel extends BaseObservable {

    public final ObservableField<String> repoName = new ObservableField<>();

    private final Repository repository;

    public RepoItemViewModel(final Repository repository) {
        this.repository = repository;
    }

    public Repository getRepository() {
        return repository;
    }
}
