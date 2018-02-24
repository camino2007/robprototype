package com.rxdroid.api.github.model

import com.google.gson.annotations.SerializedName

data class GitHubRepoData(
        @SerializedName("id") val idRep: String,
        @SerializedName("name") val name: String,
        @SerializedName("full_name") val fullName: String
)