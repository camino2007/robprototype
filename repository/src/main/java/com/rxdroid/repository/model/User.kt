package com.rxdroid.repository.model

import android.os.Parcelable
import com.rxdroid.api.github.model.GitHubUserData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        val id: Int = 1,
        val login: String = "",
        val name: String? = "",
        val type: String? = "",
        val company: String? = "",
        val email: String? = "",
        val avatarUrl: String? = "",
        val hireable: String? = "",
        val isSiteAdmin: Boolean? = false,
        val publicRepoCount: Int? = 0,
        val publicGistCount: Int? = 0,
        val repositories: List<Repository>?
) : Parcelable {
    companion object {

        fun fromApi(data: GitHubUserData): User {
            return User(id = data.id, type = data.type, isSiteAdmin = data.isSiteAdmin, hireable = data.hireable,
                    email = data.email, company = data.company, publicRepoCount = data.publicRepoCount,
                    publicGistCount = data.publicGistCount, avatarUrl = data.avatarUrl, login = data.login,
                    name = data.name, repositories = emptyList())
        }
    }

}
