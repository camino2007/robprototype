package com.rxdroid.repository.model

import android.os.Parcelable
import com.rxdroid.api.github.model.GitHubRepoData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Repository(
        val repositoryId: String,
        val fullName: String,
        val name: String) : Parcelable {

    companion object {

        fun fromApiList(dataList: List<GitHubRepoData>?): List<Repository> {
            dataList?.let {
                val repositories: ArrayList<Repository> = arrayListOf()
                if (!it.isNullOrEmpty()) {
                    it.forEach { listItem ->
                        val repository = Repository(repositoryId = listItem.idRep, name = listItem.name, fullName = listItem.fullName)
                        repositories.add(repository)
                    }
                }
                return repositories
            }
            return emptyList()
        }
    }

}
