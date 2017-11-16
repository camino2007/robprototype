package fup.prototype.data.model;

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
    public String login;

    public String name;

    public int publicRepoCount;

    public int publicGistCount;

    public RealmList<RealmRepository> repositories;

    public RealmUser() {
    }

    private RealmUser(final Builder builder) {
        login = builder.login;
        name = builder.name;
        publicRepoCount = builder.publicRepoCount;
        publicGistCount = builder.publicGistCount;
        repositories = builder.repositories;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public int getPublicRepoCount() {
        return publicRepoCount;
    }

    public int getPublicGistCount() {
        return publicGistCount;
    }

    public RealmList<RealmRepository> getRepositories() {
        return repositories;
    }

    public static RealmUser fromDomainModel(final GitHubUser gitHubUser) {
        return new Builder().login(gitHubUser.getLogin())
                            .name(gitHubUser.getName())
                            .publicGistCount(gitHubUser.getPublicGists())
                            .publicRepoCount(gitHubUser.getPublicRepos())
                            .build();
    }

    public static final class Builder {
        private String login;
        private String name;
        private int publicRepoCount;
        private int publicGistCount;
        private RealmList<RealmRepository> repositories;

        public Builder() {
        }

        public Builder login(final String val) {
            login = val;
            return this;
        }

        public Builder name(final String val) {
            name = val;
            return this;
        }

        public Builder publicRepoCount(final int val) {
            publicRepoCount = val;
            return this;
        }

        public Builder publicGistCount(final int val) {
            publicGistCount = val;
            return this;
        }

        public Builder repositories(final RealmList<RealmRepository> val) {
            repositories = val;
            return this;
        }

        public RealmUser build() {
            return new RealmUser(this);
        }
    }
}
