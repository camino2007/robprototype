package fup.prototype.robprototype.search;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.Observable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.rxdroid.api.error.RequestError;
import com.rxdroid.repository.model.User;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;

import fup.prototype.robprototype.R;
import fup.prototype.robprototype.databinding.FragmentMainBinding;
import fup.prototype.robprototype.util.DialogUtils;
import fup.prototype.robprototype.view.LiveDataViewModelFactory;
import fup.prototype.robprototype.view.base.fragments.DataFragment;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class MainFragment extends DataFragment<FragmentMainBinding, MainViewModel> {

    private static final String TAG = "MainFragment";

    @Inject
    protected LiveDataViewModelFactory liveDataViewModelFactory;

    private UserAdapter userAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public MainViewModel createViewModel() {
        return ViewModelProviders.of(this, liveDataViewModelFactory).get(MainViewModel.class);
    }

    @Override
    public void initBinding(FragmentMainBinding binding) {
        binding.setViewModel(getViewModel());
        Log.d(TAG, "initBinding: " + getViewModel().getViewState().getValue());
        binding.setLifecycleOwner(this);
        setupUserAdapter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main;
    }

    private void setupUserAdapter() {
        final RecyclerView recyclerView = getViewBinding().recyclerView;
        userAdapter = new UserAdapter();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userAdapter);
    }

    @Override
    protected AlertDialog createErrorDialog(@NonNull RequestError requestError) {
        if (requestError.getResponse() != null && requestError.getResponse().code() == HttpURLConnection.HTTP_NOT_FOUND) {
            return DialogUtils.createOkCancelDialog(getContext(), "Möp", "User not found", "Ok", "F*ck it", null, null);
        }
        if (requestError.getErrorCode() == RequestError.ERROR_CODE_NO_SEARCH_INPUT) {
            final String errorText = "If you leave this field blank, sooner or later I'll load all users.";
            return DialogUtils.createOkCancelDialog(getContext(), "ToDo", errorText, "Ok", "Fuck it", null, null);
        }
        return DialogUtils.createOkCancelDialog(getContext(), "Möp", "A wild error occurred", "Ok", "Fuck it", null, null);
    }

    @Override
    protected void addLiveDataListener() {
        super.addLiveDataListener();
        getViewModel().items.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                if (users != null && !users.isEmpty()) {
                    userAdapter.replace(users);
                }
            }
        });
    }

    @Override
    public String getKey() {
        return MainFragment.class.getSimpleName();
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
        final Disposable searchDisposable = RxTextView.textChanges(getViewBinding().input)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(@NonNull CharSequence charSequence) {
                        getViewModel().updateSearchInput(charSequence.toString());
                    }
                });
        addRxDisposable(searchDisposable);
    }

}
