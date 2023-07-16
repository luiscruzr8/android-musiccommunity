package com.tfm.musiccommunityapp.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.ProfileFragmentBinding
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.utils.getRandomColor
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment(R.layout.profile_fragment) {

    private val binding by viewBinding(ProfileFragmentBinding::bind)

    private val viewModel by viewModel<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setUp()

        observeUserInfo()
        observeFollowers()
        observeFollowing()
        observeProfileError()
        observeSignOutResult()

        binding.signoutButton.setOnClickListener {
            viewModel.signOut()
        }
    }

    private fun observeProfileError() {
        viewModel.getErrorProfile().observe(viewLifecycleOwner) { error ->
            error?.let {
                alertDialogOneOption(
                    requireContext(),
                    getString(R.string.user_alert),
                    null,
                    error,
                    getString(R.string.ok),
                    null
                )
            }
        }
    }

    private fun observeSignOutResult() {
        viewModel.getSignOutSuccess().observe(viewLifecycleOwner) {
            val action = R.id.action_global_homeScreenFragmentAfterSignOut
            navigateSafe(action)
        }
    }

    private fun observeUserInfo() {
        viewModel.getUserInfo().observe(viewLifecycleOwner) { userInfo ->
            userInfo?.let {
                binding.apply {
                    tvUsername.text =
                        String.format(getString(R.string.profile_screen_username_label), it.login)
                    tvEmail.text = it.email
                    tvPhoneNumber.text = it.phone

                    imageView.setImageDrawable(
                        AvatarGenerator.AvatarBuilder(requireContext().applicationContext)
                            .setLabel(it.login)
                            .setAvatarSize(64)
                            .setTextSize(22)
                            .toSquare()
                            .setBackgroundColor(getRandomColor())
                            .build()
                    )

                    it.bio.let { bio ->
                        if (bio.isEmpty()) {
                            tvBioLabel.visibility = View.INVISIBLE
                            tvBio.visibility = View.INVISIBLE
                        } else {
                            tvBioLabel.visibility = View.VISIBLE
                            tvBio.visibility = View.VISIBLE
                            tvBio.text = bio
                        }
                    }
                    
                    it.interests.let { interests ->
                        if (interests.isEmpty()) {
                            tvInterestsLabel.visibility = View.INVISIBLE
                            profileTagsLayout.visibility = View.INVISIBLE
                        } else {
                            tvInterestsLabel.visibility = View.VISIBLE
                            profileTagsLayout.visibility = View.VISIBLE
                            profileTagsLayout.setTagList(interests.map { it2 -> it2.tagName })
                        }
                    }
                }
            }
        }
    }

    private fun observeFollowers() {
        viewModel.getFollowers().observe(viewLifecycleOwner) { followers ->
            binding.followersButton.text =
                String.format(getString(R.string.profile_screen_followers_button), followers.size)

            binding.followersButton.setOnClickListener {
                if (followers.isNotEmpty()) {
                    Log.d("ProfileFragment", "Followers: $followers")
                } else {
                    alertDialogOneOption(
                        requireContext(),
                        getString(R.string.user_alert),
                        null,
                        getString(R.string.profile_screen_no_followers),
                        getString(R.string.ok),
                        null
                    )
                }
            }
        }
    }

    private fun observeFollowing() {
        viewModel.getFollowing().observe(viewLifecycleOwner) { following ->
            binding.followingButton.text =
                String.format(getString(R.string.profile_screen_following_button), following.size)

            binding.followingButton.setOnClickListener {
                if (following.isNotEmpty()) {
                    Log.d("ProfileFragment", "Following: $following")
                } else {
                    alertDialogOneOption(
                        requireContext(),
                        getString(R.string.user_alert),
                        null,
                        getString(R.string.profile_screen_no_following),
                        getString(R.string.ok),
                        null
                    )
                }
            }
        }
    }

}