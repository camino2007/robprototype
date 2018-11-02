package com.rxdroid.data.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import com.rxdroid.data.details.RepositoryEntity;
import io.reactivex.Maybe;

@Dao
public interface RepositoryRoomDao {

    @Query("SELECT * FROM RepositoryEntity WHERE github_user_id LIKE :userId LIMIT 1")
    Maybe<List<RepositoryEntity>> findByUserId(final int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RepositoryEntity> repositoryEntities);

    @Insert
    void insert(RepositoryEntity repositoryEntity);

    @Update
    void update(RepositoryEntity repositoryEntity);

    @Delete
    void delete(RepositoryEntity repositoryEntity);


}
