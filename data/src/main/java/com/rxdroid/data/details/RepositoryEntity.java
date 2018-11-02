package com.rxdroid.data.details;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "RepositoryEntity")
public class RepositoryEntity {

    public RepositoryEntity() {
    }

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "github_user_id")
    private int githubUserId;

    @ColumnInfo(name = "id_rep")
    private String idRep;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "full_name")
    private String fullName;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getIdRep() {
        return idRep;
    }

    public void setIdRep(final String idRep) {
        this.idRep = idRep;
    }

    public int getGithubUserId() {
        return githubUserId;
    }

    public void setGithubUserId(int githubUserId) {
        this.githubUserId = githubUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }
}
