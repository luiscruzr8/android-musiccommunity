package com.tfm.musiccommunityapp.base

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.MainActivityBinding
import com.tfm.musiccommunityapp.utils.viewBinding

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(MainActivityBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        val navigator = binding.navHostFragment.findNavController()
        binding.bottomNavigation.setupWithNavController(navigator)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeScreenFragment -> {
                    binding.navHostFragment.findNavController().navigate(R.id.homeScreenFragment)
                    true
                }

                R.id.profileFragment -> {
                    binding.navHostFragment.findNavController().navigate(R.id.profileFragment)
                    true
                }

                else -> false
            }
        }
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
        binding.apply {
            if (bottomNavigation.menu.findItem(R.id.profileFragment) != null) {
                bottomNavigation.menu.removeItem(R.id.profileFragment)
            }
        }
    }

    fun setUpLoggedInBottomNavigation() {
        binding.apply {
            if (bottomNavigation.menu.findItem(R.id.profileFragment) == null) {
                bottomNavigation.menu.add(
                    R.id.profileFragment,
                    R.id.profileFragment,
                    1,
                    getString(R.string.bottom_nav_menu_item_2)
                ).setIcon(R.drawable.bottom_nav_profile_selector)
            }
        }
    }

}