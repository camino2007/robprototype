package fup.prototype.robprototype.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import fup.prototype.robprototype.search.MainActivity;
import fup.prototype.robprototype.search.NewMainFragment;

@Module
public abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract NewMainFragment bindNewMainFragment();
}
