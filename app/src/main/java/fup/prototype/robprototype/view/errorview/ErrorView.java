package fup.prototype.robprototype.view.errorview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import com.jakewharton.rxbinding2.view.RxView;
import fup.prototype.robprototype.R;
import fup.prototype.robprototype.databinding.ViewErrorBinding;
import fup.prototype.robprototype.view.base.BaseView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ErrorView extends BaseView<ViewErrorBinding, ErrorViewModel> {

    private static final String TAG = "ErrorView";

    private OnErrorButtonListener errorListener;

    public ErrorView(Context context) {
        super(context);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public String getTag() {
        return ErrorView.class.getSimpleName();
    }

    @Override
    public ErrorViewModel createViewModel() {
        return new ErrorViewModel("title", "description");
    }

    @Override
    public void initBinding(ViewErrorBinding binding) {
        binding.setViewModel(getViewModel());
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_error;
    }

    public void setErrorListener(OnErrorButtonListener errorListener) {
        this.errorListener = errorListener;
    }

    @Override
    public void addViewListener() {
        final Disposable submitDisposable = RxView.clicks(getViewBinding().okButton).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Log.d(TAG, "accept: submitDisposable");
                if (errorListener != null) {
                    errorListener.onSubmitClicked();
                }
            }
        });
        addRxDisposable(submitDisposable);

        final Disposable discardDisposable = RxView.clicks(getViewBinding().discardButton).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Log.d(TAG, "accept: discardDisposable");
                if (errorListener != null) {
                    errorListener.onDiscardClicked();
                }
            }
        });
        addRxDisposable(discardDisposable);

        final Disposable rootLayoutDisposable = RxView.clicks(getViewBinding().errorRootLayout).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Log.d(TAG, "accept: discardDisposable");
                if (errorListener != null) {
                    errorListener.onDiscardClicked();
                }
            }
        });
        addRxDisposable(rootLayoutDisposable);
    }

    public interface OnErrorButtonListener {

        void onSubmitClicked();

        void onDiscardClicked();
    }
}
