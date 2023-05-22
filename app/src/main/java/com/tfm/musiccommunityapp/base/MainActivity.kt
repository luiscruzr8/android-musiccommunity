package com.tfm.musiccommunityapp.base

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.MainActivityBinding
import com.tfm.musiccommunityapp.utils.viewBinding

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(MainActivityBinding::inflate)

    override fun onStart() {
        super.onStart()
        binding.navHostFragment.findNavController()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.title) {
                getString(R.string.bottom_nav_menu_item_1) -> {
                    binding.navHostFragment.findNavController().navigateSafeAndRemoveBackStack(R.id.homeScreenFragment)
                    true
                }
                getString(R.string.bottom_nav_menu_item_2) -> {
                    binding.navHostFragment.findNavController().navigateSafeAndRemoveBackStack(R.id.loginFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun NavController.navigateSafeAndRemoveBackStack(@IdRes resId: Int) {
        try {
            navigate(resId, null)
            popBackStack()
        } catch (e: IllegalArgumentException) {
            Log.e("Navigator", "navigateSafe error $e")
        }
    }
}