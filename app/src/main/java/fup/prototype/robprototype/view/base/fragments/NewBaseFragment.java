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
import fup.prototype.robprototype.view.NewViewProvider;
import fup.prototype.robprototype.view.base.viewmodels.BaseLiveDataViewModel;
import fup.prototype.robprototype.view.base.viewmodels.ViewState;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public abstract class NewBaseFragment<B extends ViewDataBinding, LVM extends BaseLiveDataViewModel>
        extends Fragment implements
        NewViewProvider<B, LVM> {

    private static final String KEY_VIEW_MODEL_STATE = "keyViewModelState";

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
        if (savedInstanceState != null) {
            final ViewState viewState = (ViewState) savedInstanceState.getSerializable(KEY_VIEW_MODEL_STATE);
            this.viewModel.setViewState(viewState);
            restoreViewModelValues(savedInstanceState);
        }
        initBinding(viewBinding);
    }

    @Override
    public void onResume() {
        super.onResume();
        addViewListener();
    }

    @Override
    public void onPause() {
        removeViewListener();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_VIEW_MODEL_STATE, this.viewModel.getViewState());
        storeViewModelValues(outState);
    }

    protected void hideKeyboard() {
        final FragmentActivity activity = getActivity();
        if (activity != null) {
            final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            final View view = activity.getCurrentFocus();
            if (view != null && imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
/*    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasSupportFragmentInjector) getActivity()).supportFragmentInjector());
    }*/

    private void removeViewListener() {
        compositeDisposable.clear();
    }

    protected void addRxDisposable(@NonNull final Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    // protected abstract void injectComponent(AppComponent appComponent);

    protected abstract void storeViewModelValues(@NonNull final Bundle outState);

    protected abstract void restoreViewModelValues(@NonNull final Bundle savedInstanceState);

    public B getViewBinding() {
        return viewBinding;
    }

    public LVM getViewModel() {
        return viewModel;
    }

    public abstract String getKey();

}
