package fup.prototype.robprototype.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import fup.prototype.robprototype.view.MainActivity;

@Module
public abstract class ActivityModule
{
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();

    // all other activities defined as above
}