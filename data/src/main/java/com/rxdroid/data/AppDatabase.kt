package com.rxdroid.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.rxdroid.data.details.UserRepositoryEntity
import com.rxdroid.data.room.dao.UserDaoRoom
import com.rxdroid.data.room.dao.UserRepositoryRoomDao
import com.rxdroid.data.search.UserEntity

@Database(entities = [UserEntity::class, UserRepositoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDaoRoom(): UserDaoRoom

    abstract fun userRepositoryRoomDao(): UserRepositoryRoomDao

    companion object {

        private const val DATABASE_NAME = "database.db"

        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE!!
        }

    }

}
