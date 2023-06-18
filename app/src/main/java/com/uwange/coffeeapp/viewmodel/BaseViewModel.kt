package com.uwange.coffeeapp.viewmodel

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.uwange.coffeeapp.data.repository.CouponPointRepository
import com.uwange.coffeeapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Thread.sleep
import javax.inject.Inject
import kotlin.concurrent.thread

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val couponPointRepository: CouponPointRepository
): ViewModel() {
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

    fun firebaseAuthWithGoogle(idToken: String?, firebaseAuth: FirebaseAuth, userCallBack: (FirebaseUser) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful && firebaseAuth.currentUser != null) {
                userCallBack.invoke(firebaseAuth.currentUser!!)
            } else {
                // firebase login failure
            }
        }
    }

    fun saveUserData(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            userRepository.saveUserData(currentUser)
            couponPointRepository.insertCouponPointData(currentUser)
        }
    }
}