package fup.prototype.robprototype.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private T viewBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBinding(viewBinding);
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

    public T getViewBinding() {
        return viewBinding;
    }

    protected abstract void initBinding(T binding);

    protected abstract int getLayoutId();

    protected abstract String getKey();

}
