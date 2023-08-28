package com.tfm.musiccommunityapp.ui.community.opinions.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.OpinionDetailFragmentBinding
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.utils.formatDateTimeToString
import com.tfm.musiccommunityapp.utils.getChipColor
import com.tfm.musiccommunityapp.utils.getChipLabel
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class OpinionDetailFragment: BaseFragment(R.layout.opinion_detail_fragment) {

    private val binding by viewBinding(OpinionDetailFragmentBinding::bind)
    private val viewModel by viewModel<OpinionDetailViewModel>()
    private val args: OpinionDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val opinionId = args.id
        viewModel.setUpOpinion(opinionId)

        observeLoader()
        observeOpinionResult()
        observeScoreResult()
        observeOpinionError()
        observeIsUserOwner()
        observeOperationSuccessful()
    }

    private fun observeLoader() {
        viewModel.isOpinionLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeScoreResult() {
        viewModel.getScoreLiveData().observe(viewLifecycleOwner) { score ->
            score?.let {
                //TODO review after score implementation
                binding.pdfWebView.settings.javaScriptEnabled = true
                binding.pdfWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=$score")
            }
        }
    }

    private fun observeOpinionResult() {
        viewModel.getOpinionLiveData().observe(viewLifecycleOwner) { it ->
            it?.let { opinion ->
                binding.apply {
                    //Hidden by default
                    tvTagsLabel.isVisible = false
                    relatedTagsLayout.isVisible = false

                    tvPostChip.chipBackgroundColor =
                        getChipColor(opinion.postType, requireContext())
                    tvPostChip.text = String.format(
                        getString(R.string.chip_post),
                        opinion.id,
                        getChipLabel(opinion.postType, requireContext())
                    )

                    tvPostTitle.text = opinion.title
                    tvCreationDate.text = opinion.createdOn.formatDateTimeToString()
                    tvCreationUser.text = opinion.login

                    tvFileName.text = opinion.score.name

                    tvDescription.text = opinion.description
                    if (opinion.tags.isNotEmpty()) {
                        tvTagsLabel.isVisible = true
                        relatedTagsLayout.isVisible = true
                        relatedTagsLayout.setTagList(opinion.tags.map { it2 -> it2.tagName })
                    }
                }
            }
        }
    }

    private fun observeIsUserOwner() {
        viewModel.isUserOwnerLiveData().observe(viewLifecycleOwner) { isOwner ->
            binding.optionsButton.setOnClickListener {
                val popup = PopupMenu(requireContext(), binding.optionsButton)
                popup.menuInflater.inflate(R.menu.popup_post_options, popup.menu)

                if (!isOwner) {
                    popup.menu.removeItem(R.id.edit_option)
                    popup.menu.removeItem(R.id.delete_option)
                } else {
                    popup.menu.removeItem(R.id.recommend_option)
                }

                popup.setOnMenuItemClickListener { item: MenuItem ->
                    when (item.itemId) {
                        R.id.edit_option -> {}
                        R.id.delete_option -> deleteOpinion()
                        R.id.recommend_option -> {}
                    }
                    true
                }
                popup.show()
            }
        }
    }

    private fun observeOpinionError() {
        viewModel.getOpinionByIdError().observe(viewLifecycleOwner) { error ->
            error?.let {
                alertDialogOneOption(
                    requireContext(),
                    getString(R.string.user_alert),
                    null,
                    error,
                    getString(R.string.accept)
                ) { findNavController().popBackStack() }
            }
        }
    }

    private fun observeOperationSuccessful() {
        viewModel.isOperationSuccessfulLiveData().observe(viewLifecycleOwner) { type ->
            type?.let {
                when (type) {
                    OpinionDetailViewModel.OpinionOperationSuccess.DELETE ->
                        findNavController().popBackStack()

                    OpinionDetailViewModel.OpinionOperationSuccess.UPDATE -> {}
                }
            }
        }
    }

    private fun deleteOpinion() {
        viewModel.sendDeleteOpinion()
    }

}