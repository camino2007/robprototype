package fup.prototype.robprototype.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import fup.prototype.robprototype.view.MainFragment;

@Module
abstract class FragmentModule
{
    @ContributesAndroidInjector
    abstract MainFragment contributeMainFragment();
}
