package fup.prototype.robprototype.model;

import com.google.auto.value.AutoValue;
import com.squareup.haha.guava.collect.ImmutableList;
import fup.prototype.data.model.RealmUser;
import java.util.List;

@AutoValue
public abstract class User {

    public abstract String getName();

    public abstract ImmutableList<Repository> getRepositoryList();

    private static Builder builder() {
        return new AutoValue_User.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder setName(final String value);

        abstract Builder setRepositoryList(final List<Repository> value);

        abstract User build();
    }

    public static User fromRealm(final RealmUser realmUser) {
        return User.builder().setName(realmUser.getName()).setRepositoryList(Repository.fromRealmList(realmUser.getRepositories())).build();
    }
}
