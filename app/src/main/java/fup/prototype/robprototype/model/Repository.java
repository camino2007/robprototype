package fup.prototype.robprototype.model;

import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import fup.prototype.data.model.RealmRepository;
import io.realm.RealmList;
import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class Repository {

    public abstract String getId();

    public abstract String getName();

    public abstract String getFullName();

    private static Builder builder() {
        return new AutoValue_Repository.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {

        abstract Builder setName(final String value);

        abstract Builder setFullName(final String value);

        abstract Builder setId(final String value);

        abstract Repository build();
    }

    static List<Repository> fromRealmList(final RealmList<RealmRepository> realmRepositories) {
        final List<Repository> repositories = new ArrayList<>();
        for (RealmRepository realmRepository : realmRepositories) {
            Repository repository = create(realmRepository);
            repositories.add(repository);
        }
        return repositories;
    }

    @NonNull
    private static Repository create(RealmRepository realmRepository) {
        return Repository.builder().setId(realmRepository.getIdRep()).setName(realmRepository.getName()).setFullName(realmRepository.getFullName()).build();
    }
}
