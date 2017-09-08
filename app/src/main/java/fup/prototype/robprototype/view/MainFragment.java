package fup.prototype.robprototype.view;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import javax.inject.Inject;

import fup.prototype.robprototype.R;
import fup.prototype.robprototype.data.AppViewModelFactory;
import fup.prototype.robprototype.databinding.FragmentMainBinding;
import fup.prototype.robprototype.model.User;
import fup.prototype.robprototype.util.AutoClearedValue;
import fup.prototype.robprototype.view.adapters.RepositoryAdapter;
import fup.prototype.robprototype.view.viewmodels.MainViewModel;

public class MainFragment extends BaseFragment<FragmentMainBinding> {

    private static final String TAG = "MainFragment";

    @Inject
    protected AppViewModelFactory viewModelFactory;

    private MainViewModel mainViewModel;
    private AutoClearedValue<RepositoryAdapter> adapter;

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
        binding.setViewModel(mainViewModel);
    }

    @Override
    protected void initViewModel() {
        mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        if (mainViewModel.getUserData().getValue() != null) {
            Log.d(TAG, "initViewModel: " + mainViewModel.getUserData().getValue().getName());
        } else {
            Log.d(TAG, "initViewModel - mainViewModel.getUserData().getValue() == NULL ");
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRepositoryAdapter();
        mainViewModel.getUserData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    Log.d(TAG, "onChanged");
                    adapter.get().replace(user.getRepositoryList());
                    //doesn't work anymore
                    //mainViewModel.name.set(user.getName());
                    //works
                    //getViewBinding().userName.setText(user.getName());
                }
            }
        });
    }

    private void setupRepositoryAdapter() {
        final RecyclerView recyclerView = getViewBinding().recyclerView;
        final RepositoryAdapter repoAdapter = new RepositoryAdapter(getDataBindingComponent());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(repoAdapter);
        adapter = new AutoClearedValue<>(this, repoAdapter);
    }


}
