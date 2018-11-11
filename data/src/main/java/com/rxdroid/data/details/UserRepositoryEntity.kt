package com.rxdroid.data.details

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "RepositoryEntity")
data class UserRepositoryEntity(@PrimaryKey(autoGenerate = true) var id: Long?,
                                @ColumnInfo(name = "github_user_id") var githubUserId: Int,
                                @ColumnInfo(name = "id_rep") var idRep: String,
                                @ColumnInfo(name = "name") var name: String,
                                @ColumnInfo(name = "full_name") var fullName: String) {
    constructor() : this(null, 0, "", "", "")
}
