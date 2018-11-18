package com.rxdroid.repository.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.rxdroid.api.github.model.GitHubRepoData;

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

    public static List<Repository> fromApiList(@Nullable final List<GitHubRepoData> gitHubRepos) {
        final ArrayList<Repository> repositories = new ArrayList<>();
        if (gitHubRepos != null && !gitHubRepos.isEmpty()) {
            for (final GitHubRepoData repo : gitHubRepos) {
                final Repository repository = createFromApi(repo);
                repositories.add(repository);
            }
        }
        return repositories;
    }

    @NonNull
    private static Repository createFromApi(GitHubRepoData gitHubRepo) {
        return Repository.builder().setId(gitHubRepo.getIdRep()).setName(gitHubRepo.getName()).setFullName(gitHubRepo.getFullName()).build();
    }

}
