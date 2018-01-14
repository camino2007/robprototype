package fup.prototype.robprototype.view.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.rxdroid.api.RequestError;
import com.rxdroid.repository.model.User;
import fup.prototype.robprototype.R;
import fup.prototype.robprototype.databinding.FragmentDetailsBinding;
import fup.prototype.robprototype.util.DialogUtils;
import fup.prototype.robprototype.view.base.fragments.DataFragment;

public class DetailFragment extends DataFragment<FragmentDetailsBinding, DetailViewModel> {

    private static final String KEY_USER = "keyUser";
    private static final String KEY_USER_SAVE = "keyUserSave";

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
        final User user = (User) getArguments().getSerializable(KEY_USER);
        getViewModel().setUser(user);
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
        //nothing
    }

    @Override
    public void onResume() {
        super.onResume();
        getViewModel().loadOrShowData();
    }

    @Override
    protected AlertDialog createErrorDialog(@NonNull final RequestError requestError) {
        return DialogUtils.createOkCancelDialog(getContext(), "Möp", "A wild error occurred", "Ok", "Fuck it", null, null);
    }

    @Override
    protected void storeViewModelValues(@NonNull final Bundle outState) {
        outState.putSerializable(KEY_USER_SAVE, getViewModel().getUser());
    }

    @Override
    protected void restoreViewModelValues(@NonNull final Bundle savedInstanceState) {
        final User user = (User) savedInstanceState.getSerializable(KEY_USER);
        getViewModel().setUser(user);
    }

    @Override
    public String getKey() {
        return DetailFragment.class.getSimpleName();
    }
}
