package com.rxdroid.app.util

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.rxdroid.api.error.RequestError
import com.rxdroid.app.R

fun buildSnackBarWithClickListener(parentView: View,
                                   @StringRes textResId: Int,
                                   snackbarLength: Int,
                                   @StringRes buttonResId: Int,
                                   buttonClickListener: View.OnClickListener?): Snackbar {
    val snackbar = buildInfoSnackbar(parentView, textResId, snackbarLength)
    if (buttonClickListener != null) {
        snackbar.setAction(buttonResId, buttonClickListener)
    } else {
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }
    return snackbar
}

fun buildConfirmSnackbar(parentView: View, @StringRes textResId: Int, snackbarLength: Int): Snackbar {
    val snackbar = buildInfoSnackbar(parentView, textResId, snackbarLength)
    return snackbar.setAction("Ok") {
        snackbar.dismiss()
    }
}

fun buildErrorSnackbar(parentView: View,
                       requestError: RequestError,
                       snackbarLength: Int,
                       @StringRes buttonResId: Int,
                       buttonClickListener: View.OnClickListener?): Snackbar {
    return when (requestError) {
        is RequestError.NoResults -> buildConfirmSnackbar(parentView, R.string.error_msg_no_search_results, snackbarLength)
        is RequestError.NoConnection -> buildSnackBarWithClickListener(parentView, R.string.error_msg_no_connection, snackbarLength,
                buttonResId, buttonClickListener)
        is RequestError.CustomText -> buildSnackBarWithClickListener(parentView, requestError.errorText, snackbarLength,
                buttonResId, buttonClickListener)
        else -> buildSnackBarWithClickListener(parentView, R.string.error_msg_general, snackbarLength, buttonResId,
                buttonClickListener)
    }
}

fun buildInfoSnackbar(parentView: View, @StringRes textResId: Int, snackbarLength: Int): Snackbar {
    return Snackbar.make(parentView, textResId, snackbarLength)
}
