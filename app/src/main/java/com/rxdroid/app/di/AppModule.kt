package com.rxdroid.app.di

import apiModule
import com.rxdroid.app.details.DetailViewModel
import com.rxdroid.app.search.SearchViewModel
import com.rxdroid.data.di.dataModule
import com.rxdroid.repository.di.repositoryModule
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    viewModel { SearchViewModel(get()) }

    viewModel { DetailViewModel(get()) }
}

val appModuleList = listOf(apiModule, dataModule, repositoryModule, appModule)
