package com.rxdroid.api.github.model;

import com.google.gson.annotations.SerializedName;

public class GitHubUserModel {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("login")
    private String login;

    @SerializedName("public_repos")
    private int publicRepos;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("html_url")
    private String htmlUrl;

    @SerializedName("url")
    private String url;

    @SerializedName("type")
    private String type;

    @SerializedName("company")
    private String company;

    @SerializedName("email")
    private String email;

    @SerializedName("hireable")
    private String hireable;

    @SerializedName("site_admin")
    private boolean isSiteAdmin;

    @SerializedName("public_gists")
    private int publicGists;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public int getPublicRepos() {
        return publicRepos;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String getCompany() {
        return company;
    }

    public String getEmail() {
        return email;
    }

    public String getHireable() {
        return hireable;
    }

    public boolean isSiteAdmin() {
        return isSiteAdmin;
    }

    public int getPublicGists() {
        return publicGists;
    }
}
