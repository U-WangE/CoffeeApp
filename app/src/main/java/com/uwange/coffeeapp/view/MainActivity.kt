package com.uwange.coffeeapp.view

import android.animation.ObjectAnimator
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.uwange.coffeeapp.adapter.ImageSliderAdapter
import com.uwange.coffeeapp.databinding.ActivityMainBinding
import com.uwange.coffeeapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)

        // Splash Screen
        setupPreDrawListener()
        viewModel.forceCompleteAnimation()
        setupExitAnimation()
    }

    // PreDrawListener Add / Remove Setup Function
    private fun setupPreDrawListener() {
        val preDrawListener = object: ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                return if (viewModel.isReady) {
                    binding.root.viewTreeObserver.removeOnPreDrawListener(this)
                    true
                } else {
                    false
                }
            }
        }

        binding.root.viewTreeObserver.addOnPreDrawListener(preDrawListener)
    }

    // Exit Animation Setup Function
    private fun setupExitAnimation() {
        if (SDK_INT >= VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val alphaUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.ALPHA,
                    1f,
                    0f
                )
                alphaUp.apply {
                    interpolator = AnticipateInterpolator()
                    duration = 1000L
                }

                alphaUp.doOnEnd {
                    splashScreenView.remove()
                }

                alphaUp.start()
            }
        }
    }
}