package com.uwange.coffeeapp.viewmodel

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class MainViewModel: ViewModel() {
    var isReady = false

    // splash screen animation 종료 error 발생시 time 초 후 animation 강제 종료
    fun forceCompleteAnimation() {
        val time = if (SDK_INT >= Build.VERSION_CODES.S) 5000L else 1000L
        thread(start = true) {
            sleep(time)
            isReady = true
        }
    }
}