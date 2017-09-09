package fup.prototype.robprototype.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import fup.prototype.data.model.RealmUser;

public class User implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeList(this.repositoryList);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.repositoryList = new ArrayList<Repository>();
        in.readList(this.repositoryList, Repository.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
