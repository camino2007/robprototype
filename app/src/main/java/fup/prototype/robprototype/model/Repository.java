package fup.prototype.robprototype.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import fup.prototype.data.model.RealmRepository;
import io.realm.RealmList;

public class Repository implements Parcelable {

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
                .fullName(realmRepository.getFullName())
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.fullName);
    }

    protected Repository(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.fullName = in.readString();
    }

    public static final Parcelable.Creator<Repository> CREATOR = new Parcelable.Creator<Repository>() {
        @Override
        public Repository createFromParcel(Parcel source) {
            return new Repository(source);
        }

        @Override
        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };
}
