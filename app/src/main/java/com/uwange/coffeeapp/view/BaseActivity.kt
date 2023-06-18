package com.uwange.coffeeapp.view

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.uwange.coffeeapp.BuildConfig
import com.uwange.coffeeapp.R
import com.uwange.coffeeapp.databinding.ActivityBaseBinding
import com.uwange.coffeeapp.viewmodel.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BaseActivity : AppCompatActivity() {
    private var _binding: ActivityBaseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BaseViewModel by viewModels()

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.google_client_id)
                .requestEmail()
                .requestProfile()
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        _binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Splash Screen
        setupPreDrawListener()
        viewModel.forceCompleteAnimation()
        setupExitAnimation()

        FirebaseApp.initializeApp(applicationContext)
        setGoogleLoginLauncher()
        autoLogin()

        // navigation Setup
        setupNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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

    private fun setGoogleLoginLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    val googleSignIn = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    viewModel.firebaseAuthWithGoogle(googleSignIn.result.idToken, firebaseAuth) { user ->
                        // google && firebase login success
                        viewModel.saveUserData(user)
                    }
                } catch (e: ApiException) {
                    // google or firebase login failure
                }
            }
        }
    }

    private fun isUserLoggedIn() =
        GoogleSignIn.getLastSignedInAccount(this) != null && firebaseAuth.currentUser != null

    private fun autoLogin() {
        if (isUserLoggedIn()) {
            firebaseAuth.currentUser!!.getIdToken(true).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // login success
                    viewModel.saveUserData(firebaseAuth.currentUser)
                } else {
                    launcher.launch(
                        GoogleSignIn.getClient(this,googleSignInOptions).signInIntent)
                }
            }
        } else {
            launcher.launch(
                GoogleSignIn.getClient(this, googleSignInOptions).signInIntent)
        }
    }



    private fun setupNavigation() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.navBottomMenu.setupWithNavController(navController)

        binding.navBottomMenu.setOnItemSelectedListener { menuItem ->
            // Click Menu Item이 현재 Page의 Item인지 판단
            if (navController.currentDestination?.id != menuItem.itemId) {
                val navOptions = NavOptions.Builder().build()
                navController.navigate(menuItem.itemId, null, navOptions)
                true
            } else {
                false
            }
        }
    }
}