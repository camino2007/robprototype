package fup.prototype.robprototype.view.fragments;


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

public class MainFragment extends BaseFragment<FragmentMainBinding, MainViewModel> {

    private static final String TAG = "MainFragment";

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
    public String getKey() {
        return MainFragment.class.getSimpleName();
    }

    @Override
    protected MainViewModel createViewModel() {
        return new MainViewModel();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRepositoryAdapter();
    }

    @Override
    protected void initBinding(FragmentMainBinding binding) {
        binding.setViewModel(getViewModel());
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
                        getViewModel().observableSearchValue.set(charSequence.toString());
                    }
                });
        addRxDisposable(searchDisposable);

        final Disposable clickDisposable = RxView.clicks(getViewBinding().searchButton)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Log.d(TAG, "accept: button click");
                        getViewModel().loadData();
                    }
                });
        addRxDisposable(clickDisposable);
    }


}
