package fup.prototype.robprototype.view.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import fup.prototype.robprototype.view.ViewProvider;
import fup.prototype.robprototype.view.viewmodels.BaseViewModel;
import fup.prototype.robprototype.view.viewmodels.ViewState;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseView<B extends ViewDataBinding, VM extends BaseViewModel> extends FrameLayout implements ViewProvider<B, VM> {

    private static final String KEY_SUPER_STATE = "keySuperState";
    private static final String KEY_VIEW_MODEL_STATE = "keyViewModelState";
    private static final String KEY_REQUEST_ERROR = "keyRequestError";

    private B viewBinding;

    private VM viewModel;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BaseView(final Context context) {
        super(context);
        init();
    }

    public BaseView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        viewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayoutId(), this, true);
        initBinding(viewBinding);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(getTag(), "onAttachedToWindow");
        addViewListener();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(getTag(), "onDetachedFromWindow");
        compositeDisposable.clear();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.d(getTag(), "onRestoreInstanceState");
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            final ViewState viewState = (ViewState) bundle.getSerializable(KEY_VIEW_MODEL_STATE);
            this.viewModel.setViewState(viewState);
            state = bundle.getParcelable(KEY_SUPER_STATE);
        } else {
            this.viewModel = createViewModel();
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d(getTag(), "onSaveInstanceState");
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState());
        bundle.putSerializable(KEY_VIEW_MODEL_STATE, this.viewModel.getViewState());
        return bundle;
    }

    protected void addRxDisposable(@NonNull final Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public B getViewBinding() {
        return viewBinding;
    }

    public VM getViewModel() {
        return viewModel;
    }

    public abstract String getTag();
}
