package fup.prototype.data.model;

import java.util.List;

import fup.prototype.domain.github.model.GitHubRepo;
import fup.prototype.domain.github.model.GitHubUser;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RealmUser extends RealmObject {

    @Index
    @PrimaryKey
    private long id;

    @Required
    public String name;

    public RealmList<RealmRepository> repositories;

    public RealmUser() {
    }

    private RealmUser(Builder builder) {
        name = builder.name;
        repositories = builder.repositories;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RealmList<RealmRepository> getRepositories() {
        return repositories;
    }

    public static RealmUser fromDomainModel(final GitHubUser gitHubUser, final List<GitHubRepo> gitHubRepoList) {
        return new Builder().name(gitHubUser.getLogin())
                .repositories(RealmRepository.fromDomainModels(gitHubRepoList))
                .build();
    }

    public static final class Builder {
        private String name;
        private RealmList<RealmRepository> repositories;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder repositories(RealmList<RealmRepository> val) {
            repositories = val;
            return this;
        }

        public RealmUser build() {
            return new RealmUser(this);
        }
    }
}
