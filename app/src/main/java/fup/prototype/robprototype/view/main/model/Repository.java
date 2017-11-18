package fup.prototype.robprototype.view.main.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import fup.prototype.data.models.details.RepositoryEntity;
import fup.prototype.domain.github.model.GitHubRepo;
import io.realm.RealmList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class Repository implements Serializable {

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

    public static List<Repository> fromApiList(final List<GitHubRepo> gitHubRepos) {
        final List<Repository> repositories = new ArrayList<>();
        if (gitHubRepos != null && !gitHubRepos.isEmpty()) {
            for (final GitHubRepo repo : gitHubRepos) {
                final Repository repository = create(repo);
                repositories.add(repository);
            }
        }
        return repositories;
    }

    public static List<Repository> fromEntityList(@Nullable final RealmList<RepositoryEntity> realmRepositories) {
        final List<Repository> repositories = new ArrayList<>();
        if (realmRepositories != null && !realmRepositories.isEmpty()) {
            for (RepositoryEntity repositoryEntity : realmRepositories) {
                final Repository repository = create(repositoryEntity);
                repositories.add(repository);
            }
        }
        return repositories;
    }

    @NonNull
    private static Repository create(RepositoryEntity repositoryEntity) {
        return Repository.builder().setId(repositoryEntity.getIdRep()).setName(repositoryEntity.getName()).setFullName(repositoryEntity.getFullName()).build();
    }

    @NonNull
    private static Repository create(GitHubRepo gitHubRepo) {
        return Repository.builder().setId(gitHubRepo.getIdRep()).setName(gitHubRepo.getName()).setFullName(gitHubRepo.getFullName()).build();
    }
}
