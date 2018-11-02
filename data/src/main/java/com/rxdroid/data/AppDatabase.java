package com.rxdroid.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.rxdroid.data.details.RepositoryEntity;
import com.rxdroid.data.room.Converters;
import com.rxdroid.data.room.dao.RepositoryRoomDao;
import com.rxdroid.data.room.dao.UserRoomDao;
import com.rxdroid.data.search.UserEntity;

@Database(entities = {UserEntity.class, RepositoryEntity.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "database.db";
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(@NonNull final Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(@NonNull final Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
    }

    public abstract UserRoomDao userDao();

    public abstract RepositoryRoomDao repositoryRoomDao();
}
