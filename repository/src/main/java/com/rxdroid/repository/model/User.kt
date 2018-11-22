package com.rxdroid.repository.model

import android.os.Parcelable
import com.rxdroid.api.github.model.GitHubUserData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        val id: Int,
        val login: String = "",
        val name: String?,
        val type: String?,
        val company: String?,
        val email: String?,
        val avatarUrl: String?,
        val hireable: String?,
        val isSiteAdmin: Boolean = false,
        val publicRepoCount: Int = 0,
        val publicGistCount: Int = 0,
        val repositories: List<Repository>?
) : Parcelable {
    companion object {

        fun fromApi(data: GitHubUserData?): User {
            data?.let {
                return User(id = it.id, type = it.type, isSiteAdmin = it.isSiteAdmin, hireable = it.hireable,
                        email = it.email, company = it.company, publicRepoCount = it.publicRepoCount,
                        publicGistCount = it.publicGistCount, avatarUrl = it.avatarUrl, login = it.login,
                        name = it.name, repositories = emptyList())
            }
        }
    }

}
