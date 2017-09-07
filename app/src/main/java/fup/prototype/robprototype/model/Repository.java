package fup.prototype.robprototype.model;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import fup.prototype.data.model.RealmRepository;
import io.realm.RealmList;

public class Repository {

    private String id;
    private String name;
    private String fullName;

    private Repository(Builder builder) {
        id = builder.id;
        name = builder.name;
        fullName = builder.fullName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
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
        return new Builder()
                .id(realmRepository.getIdRep())
                .name(realmRepository.getName())
                .build();
    }


    public static final class Builder {
        private String id;
        private String name;
        private String fullName;

        Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder fullName(String val) {
            fullName = val;
            return this;
        }

        public Repository build() {
            return new Repository(this);
        }
    }
}
