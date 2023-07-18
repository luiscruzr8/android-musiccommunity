package com.tfm.musiccommunityapp.ui.profile.followers

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.FollowersFragmentBinding
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.ui.profile.ProfileViewModel
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FollowersFragment : BaseFragment(R.layout.followers_fragment) {

    private val viewModel by viewModel<ProfileViewModel>()
    private val binding by viewBinding(FollowersFragmentBinding::bind)
    private val usersAdapter = FollowersAdapter(::onUserClicked)

    private val args: FollowersFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentLabel = args.fragmentLabel
        val userProfileLogin = args.username
        viewModel.setUp(userProfileLogin)

        binding.rvFollowersOrFollowing.apply {
            adapter = usersAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        observeUsers(fragmentLabel)
        observeLoader()
    }

    private fun observeLoader() {
        viewModel.getShowLoader().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeUsers(followingOrFollowerLabel: String) {
        val followersLabel = getString(R.string.follower_screen_followers_title)
        val followingLabel = getString(R.string.follower_screen_following_title)
        when(followingOrFollowerLabel) {
            followersLabel -> {
                binding.tvFollowersOrFollowingTitle.text = followersLabel
                viewModel.getFollowers().observe(viewLifecycleOwner) { followers ->
                    followers?.let {
                        usersAdapter.setUsers(it)
                    }
                }
            }
            followingLabel -> {
                binding.tvFollowersOrFollowingTitle.text = followingLabel
                viewModel.getFollowing().observe(viewLifecycleOwner) { following ->
                    following?.let {
                        usersAdapter.setUsers(it)
                    }
                }
            }
        }
    }

    private fun onUserClicked(user: ShortUserDomain) {
        val action =
            FollowersFragmentDirections.actionFollowersFragmentToProfileFragment(user.login)
        navigateSafe(action)
    }
}