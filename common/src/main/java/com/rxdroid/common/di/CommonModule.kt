package com.rxdroid.common.di

import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module.module

val commonModule = module {

    single { Schedulers.computation() }

}
