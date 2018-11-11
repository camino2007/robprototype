package com.rxdroid.data.search

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "UserEntity")
data class UserEntity(@PrimaryKey(autoGenerate = true) var id: Long?,
                      @ColumnInfo(name = "login") var login: String,
                      @ColumnInfo(name = "name") var name: String?,
                      @ColumnInfo(name = "github_user_id") var githubUserId: Int,
                      @ColumnInfo(name = "public_repo_count") var publicRepoCount: Int,
                      @ColumnInfo(name = "public_gist_count") var publicGistCount: Int) {
    constructor() : this(null, "", "", 0, 0, 0)
}