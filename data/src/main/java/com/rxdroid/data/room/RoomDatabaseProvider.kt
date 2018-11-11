package com.rxdroid.data.room

import android.content.Context
import com.rxdroid.data.AppDatabase

abstract class RoomDatabaseProvider(context: Context) {

    val database = AppDatabase.getInstance(context)

}
