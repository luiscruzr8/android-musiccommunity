package com.tfm.musiccommunityapp.ui.base

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
    }

    override fun onStart() {
        super.onStart()

        val navigator = binding.navHostFragment.findNavController()
        binding.bottomNavigation.setupWithNavController(navigator)

        /*binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeScreenFragment -> {
                    binding.navHostFragment.findNavController().navigate(R.id.homeScreenFragment)
                    true
                }

                R.id.communityFragment -> {
                    binding.navHostFragment.findNavController().navigate(R.id.communityFragment)
                    true
                }

                R.id.scoresFragment -> {
                    binding.navHostFragment.findNavController().navigate(R.id.scoresFragment)
                    true
                }

                R.id.searchFragment -> {
                    binding.navHostFragment.findNavController().navigate(R.id.searchFragment)
                    true
                }

                R.id.profileFragment -> {
                    binding.navHostFragment.findNavController().navigate(R.id.profileFragment)
                    true
                }


                else -> false
            }
        }*/
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

}