package com.rxdroid.repository.di

import com.rxdroid.repository.repositories.search.UserSearchRepository
import com.rxdroid.repository.repositories.search.UserSearchRepositoryImpl
import org.koin.dsl.module.module

val repositoryModule = module {

    single<UserSearchRepository> { UserSearchRepositoryImpl(get(), get()) }

}

