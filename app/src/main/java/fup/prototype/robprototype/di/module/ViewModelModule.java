package fup.prototype.robprototype.di.module;

import android.arch.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import fup.prototype.robprototype.view.viewmodels.MainViewModel;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindUserViewModel(MainViewModel userViewModel);
}