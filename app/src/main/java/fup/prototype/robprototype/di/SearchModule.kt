package fup.prototype.robprototype.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import fup.prototype.robprototype.search.SearchActivity
import fup.prototype.robprototype.search.SearchFragment

@Module
abstract class SearchModule {

    @ContributesAndroidInjector
    abstract fun bindSearchActivity(): SearchActivity

    @ContributesAndroidInjector
    abstract fun bindKtSearchFragment(): SearchFragment

}