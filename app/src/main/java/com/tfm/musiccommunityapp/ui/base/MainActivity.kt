package com.tfm.musiccommunityapp.ui.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView.LABEL_VISIBILITY_LABELED
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.MainActivityBinding
import com.tfm.musiccommunityapp.ui.utils.viewBinding

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(MainActivityBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.bottomNavigation.labelVisibilityMode = LABEL_VISIBILITY_LABELED

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.notification_channel_name)
        val descriptionText = getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onStart() {
        super.onStart()

        val navigator = binding.navHostFragment.findNavController()
        binding.bottomNavigation.setupWithNavController(navigator)
    }

    fun showLoader() {
        binding.loaderLayout.isVisible = true
    }

    fun hideLoader() {
        binding.loaderLayout.isVisible = false
    }

    private fun NavController.navigateSafeAndRemoveBackStack(@IdRes resId: Int) {
        try {
            navigate(resId, null)
            popBackStack()
        } catch (e: IllegalArgumentException) {
            Log.e("Navigator", "navigateSafe error $e")
        }
    }

    fun setUpNotLoggedInBottomNavigation() {
        if (binding.bottomNavigation.menu.size > 1) {
            binding.bottomNavigation.menu.clear()
            binding.bottomNavigation.inflateMenu(R.menu.bottom_navigation_not_logged_menu)
        }
    }

    fun setUpLoggedInBottomNavigation() {
        if (binding.bottomNavigation.menu.size < 2) {
            binding.bottomNavigation.menu.clear()
            binding.bottomNavigation.inflateMenu(R.menu.bottom_navigation_logged_in_menu)
        }
    }

    companion object {
        const val CHANNEL_ID = "com.tfm.musiccommunityapp.urgent"
    }

}