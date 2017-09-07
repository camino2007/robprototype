package fup.prototype.data;


public interface RealmTable {

    String ID = "id";

    interface User {

        String NAME = "name";
        String REPOSITORIES = "repositories";
    }

    interface Repository {

        String USER_ID = "userId";
        String NAME = "name";
        String FULL_NAME = "fullName";
        String ID = "idRep";

    }

}
