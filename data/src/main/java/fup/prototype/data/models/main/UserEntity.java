package fup.prototype.data.models.main;

import fup.prototype.data.models.details.RepositoryEntity;
import fup.prototype.domain.github.model.GitHubUser;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class UserEntity extends RealmObject {

    @Index
    @PrimaryKey
    private long id;

    @Required
    public String login;

    public String name;

    public int publicRepoCount;

    public int publicGistCount;

    public RealmList<RepositoryEntity> repositories;

    public UserEntity() {
    }

    private UserEntity(final Builder builder) {
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

    public RealmList<RepositoryEntity> getRepositories() {
        return repositories;
    }

    public static UserEntity fromDomainModel(final GitHubUser gitHubUser) {
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
        private RealmList<RepositoryEntity> repositories;

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

        public Builder repositories(final RealmList<RepositoryEntity> val) {
            repositories = val;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(this);
        }
    }
}
