package com.tfm.musiccommunityapp.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
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
    }

    private fun observeUserInfo() {
        viewModel.getUserInfo().observe(viewLifecycleOwner) { userInfo ->
            userInfo?.let {
                binding.apply {
                    tvUsername.text =
                        String.format(getString(R.string.profile_screen_username_label), it.login)

                    imageView.setImageDrawable(
                        AvatarGenerator.AvatarBuilder(requireContext().applicationContext)
                            .setLabel(it.login)
                            .setAvatarSize(64)
                            .setTextSize(22)
                            .toSquare()
                            .setBackgroundColor(getRandomColor())
                            .build()
                    )

                    tvEmail.text = it.email
                    it.bio.let { bio ->
                        if (bio.isEmpty()) {
                            tvBioLabel.isVisible = false
                            tvBio.isVisible = false
                        } else {
                            tvBioLabel.isVisible = true
                            tvBio.isVisible = true
                            tvBio.text = bio
                        }
                    }
                    it.interests.let { interests ->
                        if (interests.isEmpty()) {
                            tvInterestsLabel.isVisible = false
                            profileTagsLayout.isVisible = false
                        } else {
                            tvInterestsLabel.isVisible = true
                            profileTagsLayout.isVisible = true
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