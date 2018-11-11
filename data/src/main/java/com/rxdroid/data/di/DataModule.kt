package com.rxdroid.data.di

import com.rxdroid.data.details.RepositoryDatabaseProvider
import com.rxdroid.data.details.RepositoryDatabaseProviderImpl
import com.rxdroid.data.search.UserDatabaseProvider
import com.rxdroid.data.search.UserDatabaseProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val dataModule = module {

    single { UserDatabaseProviderImpl(androidContext()) as UserDatabaseProvider }

    single { RepositoryDatabaseProviderImpl(androidContext()) as RepositoryDatabaseProvider }

}