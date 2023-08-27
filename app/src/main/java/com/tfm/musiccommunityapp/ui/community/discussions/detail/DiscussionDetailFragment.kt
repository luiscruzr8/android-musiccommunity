package com.tfm.musiccommunityapp.ui.community.discussions.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.DiscussionDetailFragmentBinding
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.utils.formatDateTimeToString
import com.tfm.musiccommunityapp.utils.getChipColor
import com.tfm.musiccommunityapp.utils.getChipLabel
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiscussionDetailFragment : BaseFragment(R.layout.discussion_detail_fragment) {

    private val binding by viewBinding(DiscussionDetailFragmentBinding::bind)
    private val viewModel by viewModel<DiscussionDetailViewModel>()
    private val args: DiscussionDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val discussionId = args.id
        viewModel.setUpDiscussion(discussionId)

        observeLoader()
        observePostImageResult()
        observeDiscussionResult()
        observeDiscussionError()
    }

    private fun observeLoader() {
        viewModel.isDiscussionLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observePostImageResult() {
        viewModel.getPostImageLiveData().observe(viewLifecycleOwner) { imageURL ->
            imageURL?.let {
                //TODO: Check why interceptor is not getting the right headers
                Glide.with(requireContext())
                    .load(imageURL)
                    .fitCenter()
                    .into(binding.ivPostImage)
            }
        }
    }

    private fun observeDiscussionResult() {
        viewModel.getDiscussionLiveData().observe(viewLifecycleOwner) { it ->
            it?.let { discussion ->
                binding.apply {
                    //Hidden by default
                    tvTagsLabel.isVisible = false
                    relatedTagsLayout.isVisible = false

                    tvPostChip.chipBackgroundColor =
                        getChipColor(discussion.postType, requireContext())
                    tvPostChip.text = String.format(
                        getString(R.string.chip_post),
                        discussion.id,
                        getChipLabel(discussion.postType, requireContext())
                    )

                    tvPostTitle.text = discussion.title
                    tvCreationDate.text = discussion.createdOn.formatDateTimeToString()
                    tvCreationUser.text = discussion.login

                    tvDescription.text = discussion.description
                    if (discussion.tags.isNotEmpty()) {
                        tvTagsLabel.isVisible = true
                        relatedTagsLayout.isVisible = true
                        relatedTagsLayout.setTagList(discussion.tags.map { it2 -> it2.tagName })
                    }
                }
            }
        }
    }

    private fun observeDiscussionError() {
        viewModel.getDiscussionByIdError().observe(viewLifecycleOwner) { error ->
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

}