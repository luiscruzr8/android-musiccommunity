package com.tfm.musiccommunityapp.ui.community.opinions

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.OpinionsFragmentBinding
import com.tfm.musiccommunityapp.domain.model.OpinionDomain
import com.tfm.musiccommunityapp.ui.community.CommunityFragmentDirections
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class OpinionsFragment: BaseFragment(R.layout.opinions_fragment) {

    private val binding by viewBinding(OpinionsFragmentBinding::bind)
    private val viewModel by viewModel<OpinionsViewModel>()
    private val opinionsAdapter = OpinionsAdapter(::onOpinionClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoader()
        observeOpinionsResult()
        observeOpinionsError()

        binding.rvCommunityOpinions.apply {
            adapter = opinionsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    fun restartViewPager(searchTerm: String?) {
        viewModel.setUpOpinions(if (searchTerm.isNullOrBlank()) null else searchTerm)
    }

    private fun observeLoader() {
        viewModel.isOpinionsLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeOpinionsResult() {
        viewModel.getOpinionsLiveData().observe(viewLifecycleOwner) { opinions ->
            opinionsAdapter.setOpinions(opinions)
            binding.noOpinionsFound.visibility =
                if (opinions.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun onOpinionClicked(opinion: OpinionDomain) {
        val action =
            CommunityFragmentDirections.actionCommunityFragmentToOpinionDetailFragment(
                opinion.id
            )
        navigateSafe(action)
    }

    private fun observeOpinionsError() {
        viewModel.getCommunityOpinionsError().observe(viewLifecycleOwner) { error ->
            binding.noOpinionsFound.text = error
        }
    }
}