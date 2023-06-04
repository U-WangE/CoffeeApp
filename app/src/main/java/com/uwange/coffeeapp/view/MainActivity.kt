package com.uwange.coffeeapp.view

import android.animation.ObjectAnimator
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.uwange.coffeeapp.databinding.ActivityMainBinding
import com.uwange.coffeeapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)


        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener {
            if (viewModel.isReady) {
                content.viewTreeObserver.removeOnDrawListener {}
                true
            } else {
                false
            }
        }

        configureSplashScreenAnimation()
    }

    private fun configureSplashScreenAnimation() {
        viewModel.forceCompleteAnimation()

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