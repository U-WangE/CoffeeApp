package com.uwange.coffeeapp.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

object CommonUtil {
    private var toast: Toast? = null
    fun showToast(context: Context, message: String) {
        if (toast != null)
            toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun errorLog(message: String?, cause: Throwable?, details: String?) {
        val logTag = "Failure to Save Coupon Save Precess"
        val logMessage = buildString {
            append("Message: $message\n")
            cause?.let { append("Cause: $it\n") }
            details?.let { append("Details: $it") }
        }
        Log.e(logTag, logMessage)
    }

}