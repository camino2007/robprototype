package fup.prototype.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import fup.prototype.data.main.UserEntity;
import io.reactivex.Maybe;
import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM UserEntity")
    Maybe<List<UserEntity>> getAll();

    @Query("SELECT * FROM UserEntity WHERE login LIKE :loginValue LIMIT 1")
    Maybe<UserEntity> findByLogin(String loginValue);

    @Insert
    void insertAll(UserEntity... users);

    @Insert
    void insert(UserEntity userEntity);

    @Update
    void update(UserEntity userEntity);

    @Delete
    void delete(UserEntity user);
}
