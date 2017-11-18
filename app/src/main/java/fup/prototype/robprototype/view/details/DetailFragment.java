package fup.prototype.robprototype.view.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import fup.prototype.domain.api.RequestError;
import fup.prototype.robprototype.R;
import fup.prototype.robprototype.databinding.FragmentDetailsBinding;
import fup.prototype.robprototype.view.base.fragments.DataFragment;
import fup.prototype.robprototype.view.main.model.User;

public class DetailFragment extends DataFragment<FragmentDetailsBinding, DetailViewModel> {

    private static final String KEY_USER = "keyUser";

    public static DetailFragment newInstance(@NonNull final Bundle bundle) {
        final DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    public static Bundle createBundle(@NonNull final User user) {
        final Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_USER, user);
        return bundle;
    }

    public DetailFragment() {
        // empty
    }

    @Override
    public DetailViewModel createViewModel() {
        return new DetailViewModel();
    }

    @Override
    public void initBinding(final FragmentDetailsBinding binding) {
        binding.setViewModel(getViewModel());
        setupRepositoryAdapter();
    }

    private void setupRepositoryAdapter() {
        final RecyclerView recyclerView = getViewBinding().recyclerView;
        final RepoAdapter repoAdapter = new RepoAdapter();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(repoAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_details;
    }

    @Override
    public void addViewListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        final User user = (User) getArguments().getSerializable(KEY_USER);
        getViewModel().setUser(user);
        getViewModel().loadOrShowData();
    }

    @Override
    protected AlertDialog createErrorDialog(@NonNull final RequestError requestError) {
        return null;
    }

    @Override
    protected void storeViewModelValues(@NonNull final Bundle outState) {

    }

    @Override
    protected void restoreViewModelValues(@NonNull final Bundle savedInstanceState) {

    }

    @Override
    public String getKey() {
        return DetailFragment.class.getSimpleName();
    }
}
