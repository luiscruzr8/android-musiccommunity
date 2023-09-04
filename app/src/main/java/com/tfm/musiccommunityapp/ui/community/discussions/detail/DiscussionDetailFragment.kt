package com.tfm.musiccommunityapp.ui.community.discussions.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.api.model.toGenericDomain
import com.tfm.musiccommunityapp.databinding.DiscussionDetailFragmentBinding
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.ui.base.BaseFragment
import com.tfm.musiccommunityapp.ui.community.comments.CommentsAdapter
import com.tfm.musiccommunityapp.ui.di.GlideApp
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.dialogs.community.CreateEditDiscussionDialog
import com.tfm.musiccommunityapp.ui.dialogs.community.CreateEditRecommendationDialog
import com.tfm.musiccommunityapp.ui.dialogs.community.PostOrRespondCommentDialog
import com.tfm.musiccommunityapp.ui.utils.formatDateToString
import com.tfm.musiccommunityapp.ui.utils.getChipColor
import com.tfm.musiccommunityapp.ui.utils.getChipLabel
import com.tfm.musiccommunityapp.ui.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiscussionDetailFragment : BaseFragment(R.layout.discussion_detail_fragment) {

    private val binding by viewBinding(DiscussionDetailFragmentBinding::bind)
    private val viewModel by viewModel<DiscussionDetailViewModel>()
    private val args: DiscussionDetailFragmentArgs by navArgs()

    private val commentsAdapter = CommentsAdapter(
        ::onResponseComment,
        ::onDeleteComment
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val discussionId = args.id
        viewModel.setUpDiscussion(discussionId)

        observeLoader()
        observePostImageResult()
        observeDiscussionResult()
        observeDiscussionError()
        observeIsUserOwner()
        observeOperationSuccessful()
        observeCommentsResult()
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
                GlideApp.with(requireContext())
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
                    tvCreationDate.text = discussion.createdOn.formatDateToString()
                    tvCreationUser.text = discussion.login

                    tvDescription.text = discussion.description
                    if (discussion.tags.isNotEmpty()) {
                        tvTagsLabel.isVisible = true
                        relatedTagsLayout.isVisible = true
                        relatedTagsLayout.setTagList(discussion.tags.map { it2 -> it2.tagName })
                    }

                    ivAddCommentButton.setOnClickListener { onAddComment() }
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
                        R.id.edit_option -> setEditDiscussionDialog()
                        R.id.delete_option -> deleteDiscussion()
                        R.id.recommend_option -> setCreateRecommendationDialog()
                    }
                    true
                }
                popup.show()
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

    private fun observeOperationSuccessful() {
        viewModel.isOperationSuccessfulLiveData().observe(viewLifecycleOwner) { type ->
            type?.let {
                when (type) {
                    DiscussionDetailViewModel.DiscussionOperationSuccess.DELETE ->
                        findNavController().popBackStack()

                    DiscussionDetailViewModel.DiscussionOperationSuccess.UPDATE ->
                        viewModel.setUpDiscussion(args.id)

                    DiscussionDetailViewModel.DiscussionOperationSuccess.RECOMMEND ->
                        findNavController().popBackStack()

                    DiscussionDetailViewModel.DiscussionOperationSuccess.COMMENT ->
                        viewModel.reloadPostComments(args.id)
                }
            }
        }
    }

    private fun observeCommentsResult() {
        viewModel.getCommentsLiveData().observe(viewLifecycleOwner) { commentList ->
            if (commentList.isNullOrEmpty()) {
                binding.noCommentsFound.isVisible = true
            } else {
                binding.noCommentsFound.isVisible = false
                binding.rvComments.apply {
                    commentsAdapter.setComments(commentList)
                    adapter = commentsAdapter
                    addItemDecoration(
                        DividerItemDecoration(
                            requireContext(),
                            DividerItemDecoration.VERTICAL
                        )
                    )
                }
            }
        }
    }

    private fun deleteDiscussion() {
        viewModel.sendDeleteDiscussion()
    }

    private fun setEditDiscussionDialog() {
        CreateEditDiscussionDialog(
            discussion = viewModel.getDiscussionLiveData().value
        ) {
            viewModel.sendUpdateDiscussion(it)
        }.show(this.parentFragmentManager, CreateEditDiscussionDialog::class.java.simpleName)
    }

    private fun setCreateRecommendationDialog() {
        viewModel.getDiscussionLiveData().value?.toGenericDomain()?.let {
            CreateEditRecommendationDialog(
                recommendation = null,
                post = it,
            ) { recommendation ->
                viewModel.sendCreateRecommendation(recommendation)
            }.show(
                this.parentFragmentManager,
                CreateEditRecommendationDialog::class.java.simpleName
            )
        }
    }

    private fun onAddComment() {
        PostOrRespondCommentDialog {
            viewModel.sendPostComment(it)
        }.show(this.parentFragmentManager, PostOrRespondCommentDialog::class.java.simpleName)
    }

    private fun onResponseComment(comment: CommentDomain) {
        PostOrRespondCommentDialog {
            viewModel.sendResponseComment(comment.id, it)
        }.show(this.parentFragmentManager, PostOrRespondCommentDialog::class.java.simpleName)
    }

    private fun onDeleteComment(comment: CommentDomain) {
        viewModel.sendDeleteComment(comment)
    }

}