package fup.prototype.robprototype.view;

import android.databinding.ViewDataBinding;

import fup.prototype.robprototype.view.base.viewmodels.BaseLiveDataViewModel;

public interface ViewProvider<B extends ViewDataBinding, LVM extends BaseLiveDataViewModel> {

    LVM createViewModel();

    void initBinding(B binding);

    int getLayoutId();

    void addViewListener();

}
