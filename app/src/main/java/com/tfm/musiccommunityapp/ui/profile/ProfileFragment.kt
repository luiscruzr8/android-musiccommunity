package com.tfm.musiccommunityapp.ui.profile

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.base.MainActivity
import com.tfm.musiccommunityapp.databinding.ProfileFragmentBinding
import com.tfm.musiccommunityapp.domain.model.UserDomain
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.dialogs.profile.EditProfileDialog
import com.tfm.musiccommunityapp.utils.getRandomColor
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment(R.layout.profile_fragment) {

    private val binding by viewBinding(ProfileFragmentBinding::bind)

    private val viewModel by viewModel<ProfileViewModel>()

    private val args: ProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userProfileLogin = args.username
        viewModel.setUp(userProfileLogin)

        observeLoader()
        observeUserInfo()
        observeFollowers(userProfileLogin)
        observeFollowing()
        observeProfileError()
        observeSignOutResult()
        observeIsMyProfile()
        observeOperationSuccessful()
        observeIsUserFollower()
    }

    private fun observeLoader() {
        viewModel.getShowLoader().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
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
                ) { findNavController().popBackStack() }
            }
        }
    }

    private fun observeSignOutResult() {
        viewModel.getSignOutSuccess().observe(viewLifecycleOwner) {
            (activity as MainActivity).setUpNotLoggedInBottomNavigation()
            val action = R.id.action_global_homeScreenFragmentAfterSignOut
            navigateSafe(action)
        }
    }

    private fun setEditDialogOnClick(userDomain: UserDomain) {
        binding.editProfileButton.setOnClickListener {
            EditProfileDialog(userDomain, ::saveEditProfile).show(
                this.parentFragmentManager,
                EditProfileDialog::class.java.simpleName
            )
        }
    }

    private fun observeUserInfo() {
        viewModel.getUserInfo().observe(viewLifecycleOwner) { userInfo ->
            userInfo?.let {
                setEditDialogOnClick(it)

                binding.apply {
                    tvUsername.text =
                        String.format(getString(R.string.profile_screen_username_label), it.login)
                    tvEmail.text = it.email
                    tvPhoneNumber.text = it.phone
                    tvUserChip.text = String.format(
                        getString(R.string.chip_user),
                        it.id
                    )
                    ivProfileImage.setImageDrawable(
                        AvatarGenerator.AvatarBuilder(requireContext().applicationContext)
                            .setLabel(it.login)
                            .setAvatarSize(80)
                            .setTextSize(24)
                            .toCircle()
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

                    userPostsButton.setOnClickListener { _ ->
                        val action = ProfileFragmentDirections
                            .actionProfileFragmentToUserPostsFragment(it.login)
                        navigateSafe(action)
                    }
                }
            }
        }
    }

    private fun observeFollowers(userProfileLogin: String?) {
        viewModel.getFollowers().observe(viewLifecycleOwner) { followers ->
            binding.followersButton.text =
                String.format(getString(R.string.profile_screen_followers_button), followers.size)

            binding.followersButton.setOnClickListener {
                if (followers.isNotEmpty()) {
                    navigateToFollowersOrFollowing(
                        getString(R.string.follower_screen_followers_title),
                        userProfileLogin
                    )
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
                    navigateToFollowersOrFollowing(getString(R.string.follower_screen_following_title))
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

    private fun navigateToFollowersOrFollowing(
        fragmentLabel: String,
        userProfileLogin: String? = null
    ) {
        val action =
            ProfileFragmentDirections.actionProfileFragmentToFollowersFragment(
                userProfileLogin,
                fragmentLabel
            )
        navigateSafe(action)
    }

    private fun saveEditProfile(userDomain: UserDomain) {
        viewModel.sendUpdateProfile(userDomain)
    }

    private fun observeIsMyProfile() {
        viewModel.isMyProfileLiveData().observe(viewLifecycleOwner) { isMyProfile ->

            binding.apply {
                tvPhoneNumber.isVisible = isMyProfile
                signOutButton.isVisible = isMyProfile
                followingButton.isVisible = isMyProfile
                editProfileButton.isVisible = isMyProfile
                followUnfollowButton.isVisible = !isMyProfile
                publicScoresButton.isVisible = !isMyProfile

                if (isMyProfile) {
                    signOutButton.setOnClickListener { viewModel.signOut() }
                } else {
                    publicScoresButton.setOnClickListener {
                        viewModel.getUserInfo().value?.login?.let { loginUsername ->
                            val action = ProfileFragmentDirections
                                .actionProfileFragmentToScoresFragment(loginUsername)
                            navigateSafe(action)
                        }
                    }
                }
            }
        }
    }

    private fun observeIsUserFollower() {
        viewModel.isUserFollowerLiveData().observe(viewLifecycleOwner) {isFollower ->
            binding.apply {
                followUnfollowButton.text = getString(
                    if (isFollower) R.string.profile_screen_unfollow_button else R.string.profile_screen_follow_button
                )

                followUnfollowButton.icon = getDrawable(
                    requireContext(),
                    if (isFollower) R.drawable.ic_unfollow else R.drawable.ic_follow
                )

                followUnfollowButton.setOnClickListener {
                    args.username?.let { username ->
                        if (isFollower) {
                            viewModel.sendUnfollowUser(username)
                        } else {
                            viewModel.sendFollowUser(username)
                        }
                    }
                }
            }

        }
    }

    private fun observeOperationSuccessful() {
        viewModel.isOperationSuccessfulLiveData().observe(viewLifecycleOwner) {
            it?.let { type ->
                when(type) {
                    ProfileViewModel.UserOperationSuccess.FOLLOW -> {
                        viewModel.setUp(args.username)
                    }
                    ProfileViewModel.UserOperationSuccess.UNFOLLOW -> {
                        viewModel.setUp(args.username)
                    }
                }
            }
        }
    }

}