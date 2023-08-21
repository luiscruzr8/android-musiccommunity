package com.tfm.musiccommunityapp.ui.community.users

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.UsersFragmentBinding
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.ui.community.CommunityFragmentDirections
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersFragment: BaseFragment(R.layout.users_fragment) {

    private val binding by viewBinding(UsersFragmentBinding::bind)
    private val viewModel by viewModel<UsersViewModel>()
    private val usersAdapter = UsersAdapter(::onUserClicked)

    override fun onStart() {
        super.onStart()
        observeLoader()
        observeUsersResult()
        observeUsersError()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        restartViewPager()

        binding.rvCommunityUsers.apply {
            adapter = usersAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    fun restartViewPager() {
        viewModel.setUpUsers()
    }

    private fun observeLoader() {
        viewModel.isUsersLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeUsersResult() {
        viewModel.getUsersLiveData().observe(viewLifecycleOwner) { users ->
            usersAdapter.setUsers(users)
            if (users.isEmpty()) {
                binding.noUsersFound.visibility = View.VISIBLE
            } else {
                binding.noUsersFound.visibility = View.GONE
            }

        }
    }

    private fun onUserClicked(user: ShortUserDomain) {
        val action = CommunityFragmentDirections.actionCommunityFragmentToProfileFragment(user.login)
        navigateSafe(action)
    }

    private fun observeUsersError() {
        viewModel.getCommunityError().observe(viewLifecycleOwner) { error ->
            binding.noUsersFound.text = error
        }
    }

    override fun onResume() {
        super.onResume()
        restartViewPager()
    }

}