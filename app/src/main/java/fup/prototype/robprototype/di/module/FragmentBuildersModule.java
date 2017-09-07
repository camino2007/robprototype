package fup.prototype.robprototype.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import fup.prototype.robprototype.view.MainFragment;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract MainFragment contributeMainFragment();
}
