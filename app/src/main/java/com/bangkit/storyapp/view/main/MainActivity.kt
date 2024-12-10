package com.bangkit.storyapp.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkit.storyapp.R
import com.bangkit.storyapp.databinding.ActivityMainBinding
import com.bangkit.storyapp.utils.LogoutCallback
import com.bangkit.storyapp.view.ViewModelFactory
import com.bangkit.storyapp.view.landing.LandingPageActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), LogoutCallback {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LandingPageActivity::class.java))
                finish()
            }
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
            )
        )

        navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            val title = when (destination.id) {
                R.id.navigation_home -> getString(R.string.title_home)
                R.id.navigation_setting -> getString(R.string.title_setting)
                else -> getString(R.string.app_name)
            }
            supportActionBar?.title = title
        }

        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onLogout() {
        viewModel.logout()
    }
}