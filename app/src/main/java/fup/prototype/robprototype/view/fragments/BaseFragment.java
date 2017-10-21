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
import fup.prototype.robprototype.view.ViewProvider;
import fup.prototype.robprototype.view.viewmodels.BaseViewModel;
import fup.prototype.robprototype.view.viewmodels.ViewState;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment<B extends ViewDataBinding, VM extends BaseViewModel> extends Fragment implements ViewProvider<B, VM> {

    private static final String KEY_VIEW_MODEL_STATE = "keyViewModelState";

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
        if (viewModel == null) {
            viewModel = createViewModel();
        }
        if (savedInstanceState != null) {
            final ViewState viewState = (ViewState) savedInstanceState.getSerializable(KEY_VIEW_MODEL_STATE);
            this.viewModel.setViewState(viewState);
            restoreViewModelValues(savedInstanceState);
        }
        initBinding(viewBinding);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_VIEW_MODEL_STATE, this.viewModel.getViewState());
        storeViewModelValues(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        addViewListener();
        viewModel.loadOrShowData();
    }

    @Override
    public void onPause() {
        removeViewListener();
        super.onPause();
    }

    private void removeViewListener() {
        compositeDisposable.clear();
    }

    protected void addRxDisposable(@NonNull final Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected abstract void storeViewModelValues(@NonNull final Bundle outState);

    protected abstract void restoreViewModelValues(@NonNull final Bundle savedInstanceState);

    public B getViewBinding() {
        return viewBinding;
    }

    public VM getViewModel() {
        return viewModel;
    }

    public abstract String getKey();
}
