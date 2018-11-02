package com.rxdroid.data.room;

import android.content.Context;
import com.rxdroid.data.AppDatabase;
import io.reactivex.annotations.NonNull;

public abstract class RoomDatabaseProvider {

    @NonNull
    private AppDatabase appDatabase;

    public RoomDatabaseProvider(@NonNull final Context context) {
        appDatabase = AppDatabase.getInstance(context);
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
