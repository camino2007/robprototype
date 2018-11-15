package com.rxdroid.data.room.dao

import androidx.room.*
import com.rxdroid.data.search.UserEntity
import io.reactivex.Maybe

@Dao
interface UserDaoRoom {

    @Query("SELECT * FROM UserEntity")
    fun getAll(): Maybe<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE login LIKE :loginValue LIMIT 1")
    fun findByLogin(loginValue: String): Maybe<UserEntity>

    @Query("SELECT * FROM UserEntity WHERE github_user_id LIKE :githubUserId LIMIT 1")
    fun findByGithubUserId(githubUserId: Int): Maybe<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userEntity: UserEntity)

    @Update
    fun update(userEntity: UserEntity)

    @Delete
    fun delete(user: UserEntity)

}