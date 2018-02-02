package fup.prototype.robprototype.search;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.rxdroid.repository.UserUiRepository;
import com.rxdroid.repository.model.User;

import fup.prototype.robprototype.view.base.viewmodels.BaseLiveDataViewModel;

public class NewMainViewModel extends BaseLiveDataViewModel {

    private static final String TAG = "NewMainViewModel";

    private MutableLiveData<String> searchValueLiveData = new MutableLiveData<>();
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> searchValue = new ObservableField<>();
    public ObservableArrayList<User> items = new ObservableArrayList<>();

    @NonNull
    private final UserUiRepository userUiRepository;

    public NewMainViewModel(@NonNull final UserUiRepository userUiRepository) {
        this.userUiRepository = userUiRepository;
        searchValue.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Log.d(TAG, "onPropertyChanged: " + searchValue.get());
                searchValueLiveData.setValue(searchValue.get());
            }
        });
    }

    public MutableLiveData<String> getSearchValueLiveData() {
        return searchValueLiveData;
    }

    public ObservableField<String> getSearchValue() {
        return searchValue;
    }
}
