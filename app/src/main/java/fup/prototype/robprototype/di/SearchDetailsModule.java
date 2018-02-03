package fup.prototype.robprototype.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import fup.prototype.robprototype.details.DetailFragment;
import fup.prototype.robprototype.details.DetailsActivity;

@Module
public abstract class SearchDetailsModule {

    @ContributesAndroidInjector
    abstract DetailsActivity bindDetailsActivity();

    @ContributesAndroidInjector
    abstract DetailFragment bindDetailFragment();

}
