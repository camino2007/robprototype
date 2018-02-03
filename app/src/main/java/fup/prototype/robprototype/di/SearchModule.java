package fup.prototype.robprototype.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import fup.prototype.robprototype.search.MainActivity;
import fup.prototype.robprototype.search.MainFragment;

@Module
public abstract class SearchModule {

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract MainFragment bindNewMainFragment();
}
