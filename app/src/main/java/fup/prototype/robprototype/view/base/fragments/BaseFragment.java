package fup.prototype.robprototype.view.base.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import dagger.android.support.AndroidSupportInjection;
import fup.prototype.robprototype.view.ViewProvider;
import fup.prototype.robprototype.view.base.viewmodels.BaseLiveDataViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public abstract class BaseFragment<B extends ViewDataBinding, LVM extends BaseLiveDataViewModel>
        extends Fragment implements ViewProvider<B, LVM> {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private B viewBinding;

    private LVM viewModel;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

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
        initBinding(viewBinding);
    }

    protected void applyLiveDataObserver() {

    }

    @Override
    public void onResume() {
        super.onResume();
        applyLiveDataObserver();
        addViewListener();
    }

    @Override
    public void onPause() {
        removeViewListener();
        super.onPause();
    }

    protected void hideKeyboard() {
        final FragmentActivity activity = getActivity();
        if (getContext() != null) {
            final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            final View view = activity.getCurrentFocus();
            if (view != null && imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void removeViewListener() {
        compositeDisposable.clear();
    }

    protected void addRxDisposable(@NonNull final Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public B getViewBinding() {
        return viewBinding;
    }

    public LVM getViewModel() {
        return viewModel;
    }

    public abstract String getKey();

}
