package com.rxdroid.data.di

import com.rxdroid.data.details.RepositoryDatabaseProvider
import com.rxdroid.data.room.RoomDatabaseProvider
import com.rxdroid.data.search.UserDatabaseProvider
import org.koin.dsl.module.module

val dataModule = module {

    single<RoomDatabaseProvider> { UserDatabaseProvider(get()) }

    single<RoomDatabaseProvider> { RepositoryDatabaseProvider(get()) }

}