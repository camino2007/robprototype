package fup.prototype.robprototype.model;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.squareup.haha.guava.collect.ImmutableList;
import fup.prototype.data.model.RealmUser;
import java.util.List;

@AutoValue
public abstract class User {

    public abstract String getLogin();

    @Nullable
    public abstract String getName();

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

        abstract Builder setRepositoryList(@Nullable final List<Repository> value);

        abstract User build();
    }

    public static User fromRealm(final RealmUser realmUser) {
        return User.builder()
                   .setLogin(realmUser.getLogin())
                   .setName(realmUser.getName())
                   .setPublicGistCount(realmUser.getPublicGistCount())
                   .setPublicRepoCount(realmUser.getPublicRepoCount())
                   .setRepositoryList(Repository.fromRealmList(realmUser.getRepositories()))
                   .build();
    }
}
