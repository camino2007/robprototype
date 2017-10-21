package fup.prototype.robprototype.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

public final class DialogUtils {

    private DialogUtils() {
    }

    public static boolean dismiss(final Dialog dialog) {
        if (isShowing(dialog)) {
            dialog.dismiss();
            return true;
        }
        return false;
    }

    private static boolean isShowing(final Dialog dialog) {
        return dialog != null && dialog.isShowing();
    }

    public static boolean showIfPossible(final Dialog dialog) {
        if (canShow(dialog)) {
            dialog.show();
            return true;
        }
        return false;
    }

    private static boolean canShow(final Dialog dialog) {
        return dialog != null && !dialog.isShowing();
    }

    public static AlertDialog createOkCancelDialog(@NonNull final Context context,
                                                   final String title,
                                                   final String message,
                                                   final String okButtonText,
                                                   final String cancelButtonText,
                                                   @Nullable final DialogInterface.OnClickListener okClickListener,
                                                   @Nullable final DialogInterface.OnClickListener cancelClickListener) {
        final AlertDialog.Builder builder = createAlertDialogBuilder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(okButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                if (okClickListener != null) {
                    okClickListener.onClick(dialog, which);
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(cancelButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                if (cancelClickListener != null) {
                    cancelClickListener.onClick(dialog, which);
                }
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    private static AlertDialog.Builder createAlertDialogBuilder(@NonNull final Context context) {
        return new AlertDialog.Builder(context);
    }
}
