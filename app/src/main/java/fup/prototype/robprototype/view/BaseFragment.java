package fup.prototype.robprototype.view;

import android.arch.lifecycle.LifecycleFragment;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fup.prototype.robprototype.di.Injectable;

public abstract class BaseFragment<T extends ViewDataBinding> extends LifecycleFragment
        implements Injectable {

    private T binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        initBinding(binding);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();
    }

    public T getBinding() {
        return binding;
    }

    protected abstract void initBinding(T binding);

    protected abstract int getLayoutId();

    protected abstract void initViewModel();

}
