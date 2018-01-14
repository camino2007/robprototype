package fup.prototype.data.details;

import io.realm.RealmObject;

public class RepositoryEntity extends RealmObject {

    private String idRep;
    private String name;
    private String fullName;

    public RepositoryEntity() {
        //needed for realm
    }

    private RepositoryEntity(Builder builder) {
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

        public RepositoryEntity build() {
            return new RepositoryEntity(this);
        }
    }


}
