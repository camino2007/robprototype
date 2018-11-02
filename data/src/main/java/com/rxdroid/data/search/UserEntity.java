package com.rxdroid.data.search;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "UserEntity", indices = {@Index(value = "login", unique = true)})
public class UserEntity {

    public UserEntity() {
    }

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "login")
    private String login;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "github_user_id")
    private int githubUserId;

    @ColumnInfo(name = "public_repo_count")
    private int publicRepoCount;

    @ColumnInfo(name = "public_gist_count")
    private int publicGistCount;

  /* private UserEntity(final Builder builder) {
    //    setId(builder.id);
        setLogin(builder.login);
        setName(builder.name);
        setPublicRepoCount(builder.publicRepoCount);
        setPublicGistCount(builder.publicGistCount);
    }*/

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getGithubUserId() {
        return githubUserId;
    }

    public void setGithubUserId(int githubUserId) {
        this.githubUserId = githubUserId;
    }

    public int getPublicRepoCount() {
        return publicRepoCount;
    }

    public void setPublicRepoCount(final int count) {
        this.publicRepoCount = count;
    }

    public int getPublicGistCount() {
        return publicGistCount;
    }

    public void setPublicGistCount(final int count) {
        this.publicGistCount = count;
    }

   /* public static final class Builder {
        private long id;
        private String login;
        private String name;
        private int publicRepoCount;
        private int publicGistCount;

        public Builder() {
        }

        public Builder id(final long val) {
            id = val;
            return this;
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

        public UserEntity build() {
            return new UserEntity(this);
        }
    }*/
}
