package fup.prototype.robprototype.view;

import android.arch.lifecycle.LifecycleFragment;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fup.prototype.robprototype.di.Injectable;
import fup.prototype.robprototype.util.AutoClearedValue;
import fup.prototype.robprototype.view.bindings.FragmentDataBindingComponent;

public abstract class BaseFragment<T extends ViewDataBinding> extends LifecycleFragment
        implements Injectable {

    private DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private AutoClearedValue<T> binding;

    private T viewBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false, dataBindingComponent);
        initBinding(viewBinding);
        binding = new AutoClearedValue<>(this, viewBinding);
        return viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();
    }

    public T getViewBinding() {
        return viewBinding;
    }

    public android.databinding.DataBindingComponent getDataBindingComponent() {
        return dataBindingComponent;
    }

    public AutoClearedValue<T> getBinding() {
        return binding;
    }

    protected abstract void initBinding(T binding);

    protected abstract int getLayoutId();

    protected abstract void initViewModel();

}
