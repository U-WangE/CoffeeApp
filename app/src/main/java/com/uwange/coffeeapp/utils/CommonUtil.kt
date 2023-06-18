package com.uwange.coffeeapp.utils

import android.util.Log

object CommonUtil {
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