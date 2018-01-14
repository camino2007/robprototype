package com.rxdroid.repository.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.rxdroid.api.github.model.GitHubRepoModel;
import fup.prototype.data.details.RepositoryEntity;
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

    public static List<Repository> fromApiList(@Nullable  final List<GitHubRepoModel> gitHubRepos) {
        final List<Repository> repositories = new ArrayList<>();
        if (gitHubRepos != null && !gitHubRepos.isEmpty()) {
            for (final GitHubRepoModel repo : gitHubRepos) {
                final Repository repository = createFromApi(repo);
                repositories.add(repository);
            }
        }
        return repositories;
    }

    public static List<Repository> fromEntityList(@Nullable final RealmList<RepositoryEntity> realmRepositories) {
        final List<Repository> repositories = new ArrayList<>();
        if (realmRepositories != null && !realmRepositories.isEmpty()) {
            for (RepositoryEntity repositoryEntity : realmRepositories) {
                final Repository repository = createFromEntity(repositoryEntity);
                repositories.add(repository);
            }
        }
        return repositories;
    }

    @NonNull
    private static Repository createFromEntity(RepositoryEntity repositoryEntity) {
        return Repository.builder().setId(repositoryEntity.getIdRep()).setName(repositoryEntity.getName()).setFullName(repositoryEntity.getFullName()).build();
    }

    @NonNull
    private static Repository createFromApi(GitHubRepoModel gitHubRepo) {
        return Repository.builder().setId(gitHubRepo.getIdRep()).setName(gitHubRepo.getName()).setFullName(gitHubRepo.getFullName()).build();
    }
}
