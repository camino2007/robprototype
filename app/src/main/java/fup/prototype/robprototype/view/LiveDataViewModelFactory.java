package fup.prototype.robprototype.view;


import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.rxdroid.repository.UserUiRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import fup.prototype.robprototype.search.NewMainViewModel;

@Singleton
public class LiveDataViewModelFactory implements ViewModelProvider.Factory {


    private final UserUiRepository userUiRepository;
    // private final AppComponent appComponent;

    @Inject
    public LiveDataViewModelFactory(@NonNull final UserUiRepository userUiRepository) {
        //  this.appComponent = appComponent;
        this.userUiRepository = userUiRepository;
    }

    @NonNull
    @Override
    public NewMainViewModel create(@NonNull Class modelClass) {
        // return appComponent.newMainViewModel();
        return new NewMainViewModel(userUiRepository);
    }

/*    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        Callable<? extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class, Callable<? extends ViewModel>> entry : creators.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }
        if (creator == null) {
            throw new IllegalArgumentException("Unknown model class " + modelClass);
        }
        try {
            return (T) creator.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/
}
