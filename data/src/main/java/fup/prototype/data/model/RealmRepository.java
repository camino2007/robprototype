package fup.prototype.data.model;


import android.support.annotation.NonNull;

import java.util.List;

import fup.prototype.domain.github.model.GitHubRepo;
import io.realm.RealmList;
import io.realm.RealmObject;

public class RealmRepository extends RealmObject {

    private String idRep;
    private String name;
    private String fullName;

    public RealmRepository() {
        //needed for realm
    }

    private RealmRepository(Builder builder) {
        idRep = builder.idRep;
        name = builder.name;
        fullName = builder.fullName;
    }

    public String getIdRep() {
        return idRep;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    @NonNull
    public static RealmList<RealmRepository> fromDomainModels(final List<GitHubRepo> gitHubRepos) {
        RealmList<RealmRepository> realmRepositories = new RealmList<>();
        for (GitHubRepo gitHubRepo : gitHubRepos) {
            RealmRepository repository = create(gitHubRepo);
            realmRepositories.add(repository);
        }
        return realmRepositories;
    }

    @NonNull
    private static RealmRepository create(GitHubRepo gitHubRepo) {
        return new Builder()
                .name(gitHubRepo.getName())
                .fullName(gitHubRepo.getFullName())
                .idRep(gitHubRepo.getIdRep())
                .build();
    }

    public static final class Builder {
        private String idRep;
        private String name;
        private String fullName;

        public Builder() {
        }

        public Builder idRep(String val) {
            idRep = val;
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

        public RealmRepository build() {
            return new RealmRepository(this);
        }
    }


}
