package com.rxdroid.app.util

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

object DialogUtils {

    fun dismiss(dialog: Dialog?): Boolean {
        if (isShowing(dialog)) {
            dialog?.dismiss()
            return true
        }
        return false
    }

    private fun isShowing(dialog: Dialog?): Boolean {
        return dialog != null && dialog.isShowing
    }

    fun showIfPossible(dialog: Dialog?): Boolean {
        if (canShow(dialog)) {
            dialog?.show()
            return true
        }
        return false
    }

    private fun canShow(dialog: Dialog?): Boolean {
        return dialog != null && !dialog.isShowing
    }

    fun createOkCancelDialog(context: Context,
                             title: String,
                             message: String,
                             okButtonText: String,
                             cancelButtonText: String,
                             okClickListener: DialogInterface.OnClickListener?,
                             cancelClickListener: DialogInterface.OnClickListener?): AlertDialog {
        val builder = createAlertDialogBuilder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(okButtonText) { dialog, which ->
            okClickListener?.onClick(dialog, which)
            dialog.dismiss()
        }
        builder.setNegativeButton(cancelButtonText) { dialog, which ->
            cancelClickListener?.onClick(dialog, which)
            dialog.dismiss()
        }
        return builder.create()
    }

    private fun createAlertDialogBuilder(context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context)
    }
}
