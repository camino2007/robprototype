package fup.prototype.robprototype.view.main.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import fup.prototype.data.models.details.RepositoryEntity;
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

    static List<Repository> fromRealmList(@Nullable final RealmList<RepositoryEntity> realmRepositories) {
        final List<Repository> repositories = new ArrayList<>();
        if (realmRepositories != null && !realmRepositories.isEmpty()) {
            for (RepositoryEntity repositoryEntity : realmRepositories) {
                Repository repository = create(repositoryEntity);
                repositories.add(repository);
            }
        }
        return repositories;
    }

    @NonNull
    private static Repository create(RepositoryEntity repositoryEntity) {
        return Repository.builder().setId(repositoryEntity.getIdRep()).setName(repositoryEntity.getName()).setFullName(repositoryEntity.getFullName()).build();
    }
}
