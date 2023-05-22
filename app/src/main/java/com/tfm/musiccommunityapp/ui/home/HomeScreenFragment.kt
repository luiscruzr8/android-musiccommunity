package com.tfm.musiccommunityapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.HomeScreenFragmentBinding
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeScreenFragment : BaseFragment(R.layout.home_screen_fragment) {

    private val binding by viewBinding(HomeScreenFragmentBinding::bind)

    private val viewModel by viewModel<HomeScreenViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeScreenShowMoreInfoButton.setOnClickListener {
            Log.e("HomeScreenFragment", "Show more info button clicked")
            navigateToMoreInformation()
        }

        binding.homeScreenLoginButton.setOnClickListener {
            Log.e("HomeScreenFragment", "Login button clicked")
            navigateToLogin()
        }
    }

    private fun navigateSafe(action: NavDirections) {
        view?.post { findNavController().navigateSafe(action) }
    }
    /*private fun navigateToProfile() {
        val action = HomeScreenFragmentDirections
        navigateSafe(action)
    }*/

    private fun navigateToMoreInformation() {
        val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToShowMoreInfoFragment()
        navigateSafe(action)
    }

    private fun navigateToLogin() {
        val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToLoginFragment()
        navigateSafe(action)
    }

}