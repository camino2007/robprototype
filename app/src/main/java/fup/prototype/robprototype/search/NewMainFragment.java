package fup.prototype.robprototype.search;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.rxdroid.api.error.RequestError;

import javax.inject.Inject;

import dagger.android.support.HasSupportFragmentInjector;
import fup.prototype.robprototype.R;
import fup.prototype.robprototype.databinding.FragmentMainNewBinding;
import fup.prototype.robprototype.view.LiveDataViewModelFactory;
import fup.prototype.robprototype.view.base.fragments.NewDataFragment;


public class NewMainFragment extends NewDataFragment<FragmentMainNewBinding, NewMainViewModel> {

    @Inject
    protected LiveDataViewModelFactory liveDataViewModelFactory;

    public static NewMainFragment newInstance() {
        return new NewMainFragment();
    }

    @Override
    public NewMainViewModel createViewModel() {
        return ViewModelProviders.of(this, liveDataViewModelFactory).get(NewMainViewModel.class);
    }

    @Override
    public void initBinding(FragmentMainNewBinding binding) {
        Log.d(getKey(), "initBinding: ");
        binding.setViewModel(getViewModel());
        setupUserAdapter();
        getViewModel().getSearchValueLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.d(getKey(), "onChanged: " + s);
            }
        });
        getViewModel().searchValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Log.d(getTag(), "onPropertyChanged: ");
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_new;
    }

    private void setupUserAdapter() {
        final RecyclerView recyclerView = getViewBinding().recyclerView;
        final UserAdapter repoAdapter = new UserAdapter();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(repoAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getKey(), "onResume");
    }

    @Override
    protected AlertDialog createErrorDialog(@NonNull RequestError requestError) {
        return null;
    }

    @Override
    protected void storeViewModelValues(@NonNull Bundle outState) {
        Log.d(getKey(), "storeViewModelValues: ");
    }

    @Override
    protected void restoreViewModelValues(@NonNull Bundle savedInstanceState) {
        Log.d(getKey(), "restoreViewModelValues: ");
    }

    @Override
    public String getKey() {
        return NewMainFragment.class.getSimpleName();
    }

    @Override
    public void addViewListener() {
        addKeyboardListener();
        addSearchInputListener();
    }

    private void addKeyboardListener() {
        getViewModel().isInProgress.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(final Observable observable, final int i) {
                if (getViewModel().isInProgress.get()) {
                    hideKeyboard();
                }
            }
        });
    }

    private void addSearchInputListener() {
     /*   final Disposable searchDisposable = RxTextView.textChanges(getViewBinding().input).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(@NonNull CharSequence charSequence) throws Exception {
                getViewModel().updateSearchInput(charSequence.toString());
            }
        });
        addRxDisposable(searchDisposable);*/
    }

    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasSupportFragmentInjector) getActivity()).supportFragmentInjector());
    }

}
