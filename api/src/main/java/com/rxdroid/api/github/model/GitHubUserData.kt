package com.rxdroid.api.github.model

import com.google.gson.annotations.SerializedName

data class GitHubUserData(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String?,
        @SerializedName("login") val login: String,
        @SerializedName("public_repos") val publicRepoCount: Int?,
        @SerializedName("avatar_url") val avatarUrl: String?,
        @SerializedName("html_url") val htmlUrl: String?,
        @SerializedName("url") val url: String?,
        @SerializedName("type") val type: String?,
        @SerializedName("company") val company: String?,
        @SerializedName("email") val email: String?,
        @SerializedName("hireable") val hireable: String?,
        @SerializedName("site_admin") val isSiteAdmin: Boolean?,
        @SerializedName("public_gists") val publicGistCount: Int?
)
