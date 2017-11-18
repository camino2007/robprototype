package fup.prototype.robprototype.view.main.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.squareup.haha.guava.collect.ImmutableList;
import fup.prototype.data.models.main.UserEntity;
import fup.prototype.domain.github.model.GitHubUser;
import java.io.Serializable;
import java.util.List;

@AutoValue
public abstract class User implements Serializable {

    public abstract int getId();

    public abstract String getLogin();

    @Nullable
    public abstract String getName();

    public abstract String getType();

    public abstract boolean getIsSiteAdmin();

    @Nullable
    public abstract String getCompany();

    @Nullable
    public abstract String getEmail();

    @Nullable
    public abstract String getAvatarUrl();

    @Nullable
    public abstract String getHireable();

    public abstract int getPublicRepoCount();

    public abstract int getPublicGistCount();

    @Nullable
    public abstract ImmutableList<Repository> getRepositoryList();

    private static Builder builder() {
        return new AutoValue_User.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder setName(@Nullable final String value);

        abstract Builder setLogin(final String value);

        abstract Builder setPublicRepoCount(final int value);

        abstract Builder setPublicGistCount(final int value);

        abstract Builder setId(final int value);

        abstract Builder setType(@Nullable final String value);

        abstract Builder setHireable(@Nullable final String value);

        abstract Builder setIsSiteAdmin(final boolean value);

        abstract Builder setEmail(@Nullable final String value);

        abstract Builder setCompany(@Nullable final String value);

        abstract Builder setAvatarUrl(@Nullable final String value);

        abstract Builder setRepositoryList(@Nullable final List<Repository> value);

        abstract User build();
    }

    @NonNull
    public static User fromApi(final GitHubUser gitHubUser) {
        return User.builder()
                   .setId(gitHubUser.getId())
                   .setLogin(gitHubUser.getLogin())
                   .setName(gitHubUser.getName())
                   .setEmail(gitHubUser.getEmail())
                   .setCompany(gitHubUser.getCompany())
                   .setType(gitHubUser.getType())
                   .setAvatarUrl(gitHubUser.getAvatarUrl())
                   .setHireable(gitHubUser.getHireable())
                   .setIsSiteAdmin(gitHubUser.isSiteAdmin())
                   .setPublicGistCount(gitHubUser.getPublicGists())
                   .setPublicRepoCount(gitHubUser.getPublicRepos())
                   .build();
    }

    @NonNull
    public static User fromEntity(final UserEntity userEntity) {
        return User.builder()
                   .setLogin(userEntity.getLogin())
                   .setName(userEntity.getName())
                   .setPublicGistCount(userEntity.getPublicGistCount())
                   .setPublicRepoCount(userEntity.getPublicRepoCount()).setRepositoryList(Repository.fromEntityList(userEntity.getRepositories()))
                   .build();
    }
}
