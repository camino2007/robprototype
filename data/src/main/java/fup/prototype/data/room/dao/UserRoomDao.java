package fup.prototype.data.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import fup.prototype.data.search.UserEntity;
import io.reactivex.Maybe;

@Dao
public interface UserRoomDao {

    @Query("SELECT * FROM UserEntity")
    Maybe<List<UserEntity>> getAll();

    @Query("SELECT * FROM UserEntity WHERE login LIKE :loginValue LIMIT 1")
    Maybe<UserEntity> findByLogin(String loginValue);

    @Query("SELECT * FROM UserEntity WHERE github_user_id LIKE :githubUserId LIMIT 1")
    Maybe<UserEntity> findByGithubUserId(final int githubUserId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity userEntity);

    @Update
    void update(UserEntity userEntity);

    @Delete
    void delete(UserEntity user);
}
