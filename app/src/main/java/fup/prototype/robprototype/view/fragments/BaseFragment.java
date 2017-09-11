package fup.prototype.robprototype.view.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fup.prototype.robprototype.view.viewmodels.BaseViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment<B extends ViewDataBinding, VM extends BaseViewModel> extends Fragment {

    private static final String KEY_VIEW_MODEL = "keyViewModel";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private B viewBinding;

    private VM viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(KEY_VIEW_MODEL);
        } else {
            viewModel = createViewModel();
        }
        initBinding(viewBinding);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_VIEW_MODEL, viewModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        addRxSubscriptions();
    }

    @Override
    public void onPause() {
        compositeDisposable.clear();
        super.onPause();
    }

    protected void addRxDisposable(@NonNull final Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected abstract void addRxSubscriptions();

    public B getViewBinding() {
        return viewBinding;
    }

    public VM getViewModel() {
        return viewModel;
    }

    protected abstract VM createViewModel();

    protected abstract void initBinding(B binding);

    protected abstract int getLayoutId();

    public abstract String getKey();

}
