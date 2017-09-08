package fup.prototype.robprototype.model;


import java.util.ArrayList;
import java.util.List;

import fup.prototype.data.model.RealmUser;

public class User {

    private String name;
    private List<Repository> repositoryList;

    private User(final Builder builder) {
        name = builder.name;
        repositoryList = builder.repositoryList;
    }

    public String getName() {
        return name;
    }

    public List<Repository> getRepositoryList() {
        return repositoryList;
    }

    public static User fromRealm(final RealmUser realmUser) {
        return new Builder(realmUser.getName())
                .repositoryList(Repository.fromRealmList(realmUser.getRepositories()))
                .build();
    }

    public static final class Builder {
        private String name = "";
        private List<Repository> repositoryList = new ArrayList<>();

        public Builder(final String val) {
            name = val;
        }

        public Builder repositoryList(List<Repository> val) {
            repositoryList = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
