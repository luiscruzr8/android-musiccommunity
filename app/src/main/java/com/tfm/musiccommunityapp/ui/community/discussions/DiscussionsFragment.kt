package com.tfm.musiccommunityapp.ui.community.discussions

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.DiscussionsFragmentBinding
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.ui.base.BaseFragment
import com.tfm.musiccommunityapp.ui.community.CommunityFragmentDirections
import com.tfm.musiccommunityapp.ui.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiscussionsFragment: BaseFragment(R.layout.discussions_fragment) {

    private val binding by viewBinding(DiscussionsFragmentBinding::bind)
    private val viewModel by viewModel<DiscussionsViewModel>()
    private val discussionsAdapter = DiscussionsAdapter(::onDiscussionClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoader()
        observeDiscussionsResult()
        observeDiscussionsError()

        binding.rvCommunityDiscussions.apply {
            adapter = discussionsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    fun restartViewPager(searchTerm: String?) {
        viewModel.setUpDiscussions(if (searchTerm.isNullOrBlank()) null else searchTerm)
    }

    private fun observeLoader() {
        viewModel.isDiscussionsLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeDiscussionsResult() {
        viewModel.getDiscussionsLiveData().observe(viewLifecycleOwner) { discussions ->
            discussionsAdapter.setDiscussions(discussions)
            binding.noDiscussionsFound.visibility =
                if (discussions.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun onDiscussionClicked(discussion: DiscussionDomain) {
        val action =
            CommunityFragmentDirections.actionCommunityFragmentToDiscussionDetailFragment(discussion.id)
        navigateSafe(action)
    }

    private fun observeDiscussionsError() {
        viewModel.getCommunityDiscussionsError().observe(viewLifecycleOwner) { error ->
            binding.noDiscussionsFound.text = error
        }
    }
}