package com.rxdroid.api.github.model;

import com.google.gson.annotations.SerializedName;

public class GitHubRepoModel {

    @SerializedName("id")
    private String idRep;

    @SerializedName("name")
    private String name;

    @SerializedName("full_name")
    private String fullName;

    public String getIdRep() {
        return idRep;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

}
