package com.tfm.musiccommunityapp.ui.profile.recommendations

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.UserRecommendationsFragmentBinding
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.ui.community.recommendations.RecommendationsAdapter
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.profile.ProfileViewModel
import com.tfm.musiccommunityapp.ui.profile.posts.UserPostsFragment
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserRecommendationsFragment: BaseFragment(R.layout.user_recommendations_fragment) {

    private val binding by viewBinding(UserRecommendationsFragmentBinding::bind)
    private val viewModel by viewModel<ProfileViewModel>()
    private val args by navArgs<UserRecommendationsFragmentArgs>()
    private val recommendationsAdapter = RecommendationsAdapter(::onRecommendationClicked)

    private var searchTerm = ""
    private var getTop10 = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(UserPostsFragment.SEARCH_TERM, searchTerm)
        outState.putBoolean(UserPostsFragment.POST_TYPE, getTop10)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            searchTerm = savedInstanceState.getCharSequence(SEARCH_TERM, "").toString()
            getTop10 = savedInstanceState.getBoolean(GET_TOP_10, false)
            binding.searchEditText.setText(searchTerm)
            binding.isTop10CheckBox.isChecked = getTop10
        }

        observeLoader()
        observeRecommendationsResult()
        observeRecommendationsError()

        viewModel.setUpRecommendations(
            args.login,
            getTop10,
            searchTerm
        )

        binding.rvUserRecommendations.apply {
            adapter = recommendationsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        binding.tvRecommendationsTitle.text = getString(
            R.string.profile_screen_title_user_recommendations,
            args.login
        )

        binding.isTop10CheckBox.setOnCheckedChangeListener { _, isChecked ->
            getTop10 = isChecked
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchTerm = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.submitSearchButton.setOnClickListener {
            viewModel.setUpRecommendations(
                args.login,
                getTop10,
                searchTerm
            )
        }
    }

    private fun observeLoader() {
        viewModel.getShowLoader().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeRecommendationsResult() {
        viewModel.getUserRecommendations().observe(viewLifecycleOwner) { recommendations ->
            binding.noRecommendationsFound.isVisible = recommendations.isNullOrEmpty()
            recommendations?.let {
                recommendationsAdapter.setRecommendations(it)
            }
        }
    }

    private fun observeRecommendationsError() {
        viewModel.getUserRecommendationsError().observe(viewLifecycleOwner) { error ->
            error?.let {
                alertDialogOneOption(
                    requireContext(),
                    getString(R.string.user_alert),
                    null,
                    error,
                    getString(R.string.ok),
                ) { }
            }
        }
    }

    private fun onRecommendationClicked(recommendation: RecommendationDomain) {
        val action = UserRecommendationsFragmentDirections
            .actionUserRecommendationsFragmentToRecommendationDetailFragment(recommendation.id)
        navigateSafe(action)
    }

    companion object {
        const val SEARCH_TERM = "searchTerm"
        const val GET_TOP_10 = "getTop10"
    }
}