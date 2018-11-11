package com.rxdroid.repository.di

import com.rxdroid.repository.repositories.detail.DetailsRepository
import com.rxdroid.repository.repositories.detail.DetailsRepositoryImpl
import com.rxdroid.repository.repositories.search.UserSearchRepository
import com.rxdroid.repository.repositories.search.UserSearchRepositoryImpl
import org.koin.dsl.module.module

val repositoryModule = module {

    single { UserSearchRepositoryImpl(get(), get()) as UserSearchRepository }

    single { DetailsRepositoryImpl(get(), get()) as DetailsRepository }

}

