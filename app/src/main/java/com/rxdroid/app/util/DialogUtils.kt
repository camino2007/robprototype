package com.rxdroid.app.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.rxdroid.api.error.NoConnectionRequestError
import com.rxdroid.api.error.NoResultsError
import com.rxdroid.api.error.RequestError
import com.rxdroid.app.R

fun getErrorDialog(context: Context, requestError: RequestError): AlertDialog {
    return when (requestError) {
        is NoResultsError -> createErrorDialog(context, R.string.error_title, R.string.error_msg_no_search_results)
        is NoConnectionRequestError -> createErrorDialog(context, R.string.error_title, R.string.error_msg_no_connection)
        else -> createErrorDialog(context, R.string.error_title, R.string.error_msg_general)
    }
}

private fun createErrorDialog(context: Context, titleResId: Int, messageResId: Int): AlertDialog {
    return AlertDialog.Builder(context)
            .setTitle(titleResId)
            .setMessage(messageResId)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
}
