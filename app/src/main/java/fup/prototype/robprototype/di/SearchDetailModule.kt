package fup.prototype.robprototype.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import fup.prototype.robprototype.details.DetailActivity
import fup.prototype.robprototype.details.DetailFragment

@Module
abstract class SearchDetailModule {

    @ContributesAndroidInjector
    abstract fun bindDetailActivity(): DetailActivity

    @ContributesAndroidInjector
    abstract fun bindDetailFragment(): DetailFragment

}