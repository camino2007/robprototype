package fup.prototype.robprototype.view;

import android.databinding.ViewDataBinding;
import fup.prototype.robprototype.view.base.viewmodels.BaseViewModel;

public interface ViewProvider<B extends ViewDataBinding, VM extends BaseViewModel> {

    VM createViewModel();

    void initBinding(B binding);

    int getLayoutId();

    void addViewListener();
}
