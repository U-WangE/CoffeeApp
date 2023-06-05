package com.uwange.coffeeapp.viewmodel

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class MainViewModel: ViewModel() {
    var isReady = false

    // splash screen animation 종료 error 발생시 time 초 후 animation 강제 종료
    fun forceCompleteAnimation() {
        if (SDK_INT >= VERSION_CODES.S) {
            isReady = true
        }
        else {
            thread(start = true) {
                sleep(1000L)
                isReady = true
            }
        }
    }
}