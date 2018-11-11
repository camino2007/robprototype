package com.rxdroid.data.room.dao

import android.arch.persistence.room.*
import com.rxdroid.data.details.UserRepositoryEntity
import io.reactivex.Maybe

@Dao
interface UserRepositoryRoomDao {

    @Query("SELECT * FROM RepositoryEntity WHERE github_user_id LIKE :userId LIMIT 1")
    fun findByUserId(userId: Int): Maybe<List<UserRepositoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repositoryEntities: List<UserRepositoryEntity>)

    @Insert
    fun insert(repositoryEntity: UserRepositoryEntity)

    @Update
    fun update(repositoryEntity: UserRepositoryEntity)

    @Delete
    fun delete(repositoryEntity: UserRepositoryEntity)

}