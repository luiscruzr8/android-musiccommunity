package com.tfm.musiccommunityapp.ui.home

import android.os.Bundle
import android.view.View
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.base.MainActivity
import com.tfm.musiccommunityapp.databinding.HomeScreenFragmentBinding
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeScreenFragment : BaseFragment(R.layout.home_screen_fragment) {

    private val binding by viewBinding(HomeScreenFragmentBinding::bind)

    private val viewModel by viewModel<HomeScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showLoader()
        viewModel.setUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeCurrentUser()

        binding.homeScreenShowMoreInfoButton.setOnClickListener {
            navigateToMoreInformation()
        }
    }

    private fun observeCurrentUser() {
        viewModel.getCurrentUserLiveData().observe(viewLifecycleOwner) { user ->
            binding.apply {
                user?.let {
                    homeScreenTitle.text = String.format(
                        getString(R.string.home_screen_title_already_signed_in),
                        user.login
                    )
                    homeScreenSubtitle.text =
                        getString(R.string.home_screen_subtitle_already_signed_in)
                    homeScreenLoginButton.text = getString(R.string.home_screen_profile_button)
                    homeScreenLoginButton.setOnClickListener {
                        navigateToLoginOrProfile(true)
                    }
                    (activity as MainActivity).setUpLoggedInBottomNavigation()
                } ?: run {
                    homeScreenTitle.text = getString(R.string.home_screen_title)
                    homeScreenSubtitle.text = getString(R.string.home_screen_subtitle)
                    homeScreenLoginButton.text = getString(R.string.home_screen_login_button)
                    homeScreenLoginButton.setOnClickListener {
                        navigateToLoginOrProfile(false)
                    }
                    (activity as MainActivity).setUpNotLoggedInBottomNavigation()
                }
                hideLoader()
            }
        }
    }

    private fun navigateToMoreInformation() {
        val direction =
            HomeScreenFragmentDirections.actionHomeScreenFragmentToShowMoreInfoFragment()
        navigateSafe(direction)
    }

    private fun navigateToLoginOrProfile(alreadySignedIn: Boolean) {
        val direction = if (alreadySignedIn) {
            HomeScreenFragmentDirections.actionHomeScreenFragmentToProfileFragment(null)
        } else {
            HomeScreenFragmentDirections.actionHomeScreenFragmentToLoginFragment()
        }
        navigateSafe(direction)
    }

}