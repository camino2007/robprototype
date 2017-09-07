package fup.prototype.robprototype.view;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import fup.prototype.robprototype.R;
import fup.prototype.robprototype.data.AppViewModelFactory;
import fup.prototype.robprototype.databinding.FragmentMainBinding;
import fup.prototype.robprototype.model.Repository;
import fup.prototype.robprototype.util.AutoClearedValue;
import fup.prototype.robprototype.view.adapters.RepositoryAdapterNew;
import fup.prototype.robprototype.view.bindings.FragmentDataBindingComponent;

public class MainFragment extends BaseFragment<FragmentMainBinding> {

    private static final String TAG = "MainFragment";

    @Inject
    AppViewModelFactory viewModelFactory;

    private MainViewModel mainViewModel;
    private DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private AutoClearedValue<RepositoryAdapterNew> adapter;

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
    protected void initBinding(FragmentMainBinding binding) {
        Log.d(TAG, "initBinding");
        binding.setViewModel(mainViewModel);
    }

    @Override
    protected void initViewModel() {
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        Log.d(TAG, "initViewModel: " + mainViewModel.items.size());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRepositoryAdapter();
        mainViewModel.getResults().observe(this, new Observer<List<Repository>>() {
            @Override
            public void onChanged(@Nullable List<Repository> repositories) {
                adapter.get().replace(repositories);
                Log.d(TAG, "onChanged");
            }
        });

    }

    private void setupRepositoryAdapter() {
        final RecyclerView recyclerView = getBinding().recyclerView;
        final RepositoryAdapterNew repoAdapter = new RepositoryAdapterNew(dataBindingComponent);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(repoAdapter);
        adapter = new AutoClearedValue<>(this, repoAdapter);
    }


}
