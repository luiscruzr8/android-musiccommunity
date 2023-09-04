package com.tfm.musiccommunityapp.ui.community.recommendations

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.RecommendationsFragmentBinding
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.ui.base.BaseFragment
import com.tfm.musiccommunityapp.ui.community.CommunityFragmentDirections
import com.tfm.musiccommunityapp.ui.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecommendationsFragment: BaseFragment(R.layout.recommendations_fragment) {

    private val binding by viewBinding(RecommendationsFragmentBinding::bind)
    private val viewModel by viewModel<RecommendationsViewModel>()
    private val recommendationsAdapter = RecommendationsAdapter(::onRecommendationClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoader()
        observeRecommendationsResult()
        observeRecommendationsError()

        binding.rvCommunityRecommendations.apply {
            adapter = recommendationsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    fun restartViewPager(isTop10Selected: Boolean, searchTerm: String?) {
        viewModel.setUpRecommendations(isTop10Selected, if (searchTerm.isNullOrBlank()) null else searchTerm)
    }

    private fun observeLoader() {
        viewModel.isRecommendationsLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeRecommendationsResult() {
        viewModel.getRecommendationsLiveData().observe(viewLifecycleOwner) { recommendations ->
            recommendationsAdapter.setRecommendations(recommendations)
            binding.noRecommendationsFound.visibility =
                if (recommendations.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun onRecommendationClicked(recommendation: RecommendationDomain) {
        val action = CommunityFragmentDirections
            .actionCommunityFragmentToRecommendationDetailFragment(recommendation.id)
        navigateSafe(action)
    }

    private fun observeRecommendationsError() {
        viewModel.getCommunityRecommendationsError().observe(viewLifecycleOwner) { error ->
            binding.noRecommendationsFound.text = error
        }
    }

}