package fup.prototype.data.realm;

public interface RealmTable {

    String ID = "id";

    interface User {

        String NAME = "name";
        String LOGIN = "login";
        String PUBLIC_REPOS_COUNTER = "publicReposCounter";
        String PUBLIC_GISTS_COUNTER = "publicGistsCounter";
        String REPOSITORIES = "repositories";
    }

    interface Repository {

        String USER_ID = "userId";
        String NAME = "name";
        String FULL_NAME = "fullName";
        String ID = "idRep";
    }
}
