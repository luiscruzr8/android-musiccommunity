package com.tfm.musiccommunityapp.ui.search.tag

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.SearchUsersByInterestsFragmentBinding
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.profile.followers.FollowersAdapter
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchUsersByInterestFragment : BaseFragment(R.layout.search_users_by_interests_fragment) {

    private val binding by viewBinding(SearchUsersByInterestsFragmentBinding::bind)
    private val viewModel by viewModel<SearchByTagViewModel>()
    private val usersAdapter = FollowersAdapter(::onUserClicked)

    private var interestNameSearch = ""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(INTEREST_NAME_SEARCH, interestNameSearch)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            interestNameSearch = savedInstanceState.getCharSequence(INTEREST_NAME_SEARCH, "").toString()
            binding.interestNameEditText.setText(interestNameSearch)
        }

        observeLoader()
        observeTagsResult()
        observeTagsError()
        observeUsersResult()
        observeUsersError()

        viewModel.setUpSearchByInterests(interestNameSearch)
        binding.rvUsers.apply {
            adapter = usersAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        (binding.interestNameEditText as? MaterialAutoCompleteTextView)
            ?.setOnItemClickListener  { parent, _, position, _ ->
                interestNameSearch = parent.getItemAtPosition(position) as String
            }

        binding.submitSearchButton.setOnClickListener {
            viewModel.setUpSearchByInterests(interestNameSearch)
        }
    }

    private fun observeLoader() {
        viewModel.isLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeTagsResult() {
        viewModel.getTagsOrInterestsResult().observe(viewLifecycleOwner) { tags ->
            tags?.let {
                (binding.interestNameEditText as? MaterialAutoCompleteTextView)
                    ?.setSimpleItems(tags.map { it.tagName }.toTypedArray())
            }
        }
    }

    private fun observeTagsError() {
        viewModel.getTagsOrInterestsErrors().observe(viewLifecycleOwner) { error ->
            error?.let {
                alertDialogOneOption(
                    requireContext(),
                    getString(R.string.user_alert),
                    null,
                    error,
                    getString(R.string.ok),
                ) {  }
            }
        }
    }

    private fun observeUsersResult() {
        viewModel.getUsersResult().observe(viewLifecycleOwner) { users ->
            binding.noUsersFound.isVisible = users.isNullOrEmpty()
            users?.let {
                usersAdapter.setUsers(users)
            }
        }
    }

    private fun observeUsersError() {
        viewModel.getPostsErrors().observe(viewLifecycleOwner) { error ->
            error?.let {
                alertDialogOneOption(
                    requireContext(),
                    getString(R.string.user_alert),
                    null,
                    error,
                    getString(R.string.ok),
                ) {  }
            }
        }
    }


    private fun onUserClicked(user: ShortUserDomain) {
        val action = SearchUsersByInterestFragmentDirections
            .actionSearchUsersByInterestFragmentToProfileFragment(user.login)
        navigateSafe(action)
    }

    companion object {
        const val INTEREST_NAME_SEARCH = "interestNameSearch"
    }

}