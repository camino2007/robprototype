package fup.prototype.robprototype.search;

import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.rxdroid.api.error.RequestError;
import com.rxdroid.repository.UserUiRepository;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import fup.prototype.robprototype.R;
import fup.prototype.robprototype.databinding.FragmentMainBinding;
import fup.prototype.robprototype.di.AppComponent;
import fup.prototype.robprototype.util.DialogUtils;
import fup.prototype.robprototype.view.base.fragments.DataFragment;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainFragment extends DataFragment<FragmentMainBinding, MainViewModel> {

    private static final String KEY_SEARCH_VALUE = "keySearchValue";

    @Inject
    protected UserUiRepository userUiRepository;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public MainFragment() {
        // Requires empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public String getKey() {
        return MainFragment.class.getSimpleName();
    }

    @Override
    public MainViewModel createViewModel() {
        return new MainViewModel(userUiRepository);
    }

    @Override
    public void initBinding(final FragmentMainBinding binding) {
        Log.d(getKey(), "initBinding");
        binding.setViewModel(getViewModel());
        setupUserAdapter();
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
        getViewModel().loadOrShowData();
    }

    @Override
    protected void injectComponent(final AppComponent appComponent) {
        //  appComponent.inject(this);
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
        final Disposable searchDisposable = RxTextView.textChanges(getViewBinding().input).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(@NonNull CharSequence charSequence) throws Exception {
                getViewModel().updateSearchInput(charSequence.toString());
            }
        });
        addRxDisposable(searchDisposable);
    }

    @Override
    protected void storeViewModelValues(@NonNull final Bundle outState) {
        outState.putString(KEY_SEARCH_VALUE, getViewModel().searchValue.get());
    }

    @Override
    protected void restoreViewModelValues(@NonNull final Bundle savedInstanceState) {
        final String searchValue = savedInstanceState.getString(KEY_SEARCH_VALUE);
        Log.d(getKey(), "restoreViewModelValues: " + searchValue);
        getViewModel().searchValue.set(searchValue);
    }

    @Override
    protected AlertDialog createErrorDialog(@NonNull final RequestError requestError) {
        if (requestError.getResponse() != null && requestError.getResponse().code() == HttpURLConnection.HTTP_NOT_FOUND) {
            return DialogUtils.createOkCancelDialog(getContext(), "Möp", "User not found", "Ok", "Fuck it", null, null);
        }
        if (requestError.getErrorCode() == RequestError.ERROR_CODE_NO_SEARCH_INPUT) {
            final String errorText = "If you leave this field blank, sooner or later I'll load all users.";
            return DialogUtils.createOkCancelDialog(getContext(), "ToDo", errorText, "Ok", "Fuck it", null, null);
        }
        return DialogUtils.createOkCancelDialog(getContext(), "Möp", "A wild error occurred", "Ok", "Fuck it", null, null);
    }
}
