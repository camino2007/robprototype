package fup.prototype.domain.github.model;

import com.google.gson.annotations.SerializedName;

public class GitHubUser {

    @SerializedName("name")
    private String name;

    @SerializedName("login")
    private String login;

    @SerializedName("public_repos")
    private int publicRepos;

    @SerializedName("public_gists")
    private int publicGists;

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public int getPublicRepos() {
        return publicRepos;
    }

    public int getPublicGists() {
        return publicGists;
    }
}
