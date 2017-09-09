package fup.prototype.robprototype.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import fup.prototype.robprototype.R;
import fup.prototype.robprototype.databinding.FragmentMainBinding;
import fup.prototype.robprototype.view.adapters.RepositoryAdapter;
import fup.prototype.robprototype.view.viewmodels.MainViewModel;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainFragment extends BaseFragment<FragmentMainBinding> {

    private static final String TAG = "MainFragment";
    private static final String KEY_VIEW_MODEL = "keyViewModel";

    private MainViewModel mainViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public MainFragment() {
        // Requires empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected String getKey() {
        return MainFragment.class.getSimpleName();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setupRepositoryAdapter();
        if (savedInstanceState != null) {
            mainViewModel = savedInstanceState.getParcelable(KEY_VIEW_MODEL);
        } else {
            mainViewModel = new MainViewModel();
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_VIEW_MODEL, mainViewModel);
    }

    @Override
    protected void initBinding(FragmentMainBinding binding) {
        binding.setViewModel(mainViewModel);
    }


    private void setupRepositoryAdapter() {
        final RecyclerView recyclerView = getViewBinding().recyclerView;
        final RepositoryAdapter repoAdapter = new RepositoryAdapter();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(repoAdapter);
    }

    @Override
    protected void addRxSubscriptions() {
        final Disposable searchDisposable = RxTextView.textChanges(getViewBinding().input)
                .skipInitialValue()
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(@NonNull CharSequence charSequence) throws Exception {
                        Log.d(TAG, "accept: " + charSequence.toString());
                        mainViewModel.observableSearchValue.set(charSequence.toString());
                    }
                });
        addRxDisposable(searchDisposable);

        final Disposable clickDisposable = RxView.clicks(getViewBinding().searchButton)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Log.d(TAG, "accept: button click");
                        mainViewModel.loadData();
                    }
                });
        addRxDisposable(clickDisposable);
    }
}
