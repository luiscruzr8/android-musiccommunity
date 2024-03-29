package com.tfm.musiccommunityapp.ui.community.opinions.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.api.model.toGenericDomain
import com.tfm.musiccommunityapp.databinding.OpinionDetailFragmentBinding
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.ui.base.BaseFragment
import com.tfm.musiccommunityapp.ui.community.comments.CommentsAdapter
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.dialogs.community.CreateEditOpinionDialog
import com.tfm.musiccommunityapp.ui.dialogs.community.CreateEditRecommendationDialog
import com.tfm.musiccommunityapp.ui.dialogs.community.PostOrRespondCommentDialog
import com.tfm.musiccommunityapp.ui.utils.formatDateToString
import com.tfm.musiccommunityapp.ui.utils.getChipColor
import com.tfm.musiccommunityapp.ui.utils.getChipLabel
import com.tfm.musiccommunityapp.ui.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class OpinionDetailFragment: BaseFragment(R.layout.opinion_detail_fragment) {

    private val binding by viewBinding(OpinionDetailFragmentBinding::bind)
    private val viewModel by viewModel<OpinionDetailViewModel>()
    private val args: OpinionDetailFragmentArgs by navArgs()

    private val commentsAdapter = CommentsAdapter(
        ::onResponseComment,
        ::onDeleteComment
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val opinionId = args.id
        viewModel.setUpOpinion(opinionId)

        observeLoader()
        observeOpinionResult()
        observeOpinionError()
        observeScoreFileResult()
        observeScoreFileError()
        observeIsUserOwner()
        observeOperationSuccessful()
        observeCommentsResult()
    }

    private fun observeLoader() {
        viewModel.isOpinionLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
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
                    tvCreationDate.text = opinion.createdOn.formatDateToString()
                    tvCreationUser.text = opinion.login

                    tvFileName.text = opinion.score.name

                    tvDescription.text = opinion.description
                    if (opinion.tags.isNotEmpty()) {
                        tvTagsLabel.isVisible = true
                        relatedTagsLayout.isVisible = true
                        relatedTagsLayout.setTagList(opinion.tags.map { it2 -> it2.tagName })
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
                        R.id.edit_option -> setEditOpinionDialog()
                        R.id.delete_option -> deleteOpinion()
                        R.id.recommend_option -> setCreateRecommendationDialog()
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

    private fun observeScoreFileResult() {
        viewModel.getScoreFileLiveData().observe(viewLifecycleOwner) { scoreFile ->
            scoreFile?.let {
                binding.scoreFilePdfView
                    .fromFile(it)
                    .enableSwipe(true)
                    .swipeHorizontal(true)
                    .enableAntialiasing(true)
                    .pageSnap(true)
                    .autoSpacing(true)
                    .pageFitPolicy(FitPolicy.BOTH)
                    .pageFling(true)
                    .load()
            }
        }
    }

    private fun observeScoreFileError() {
        viewModel.getScoreFileError().observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                alertDialogOneOption(
                    requireContext(),
                    it,
                    null,
                    errorMessage,
                    getString(R.string.accept)
                ) { }
            }
        }
    }

    private fun observeOperationSuccessful() {
        viewModel.isOperationSuccessfulLiveData().observe(viewLifecycleOwner) { type ->
            type?.let {
                when (type) {
                    OpinionDetailViewModel.OpinionOperationSuccess.DELETE ->
                        findNavController().popBackStack()

                    OpinionDetailViewModel.OpinionOperationSuccess.UPDATE ->
                        viewModel.setUpOpinion(args.id)

                    OpinionDetailViewModel.OpinionOperationSuccess.RECOMMEND ->
                        findNavController().popBackStack()

                    OpinionDetailViewModel.OpinionOperationSuccess.COMMENT ->
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

    private fun deleteOpinion() {
        viewModel.sendDeleteOpinion()
    }

    private fun setEditOpinionDialog() {
        CreateEditOpinionDialog(
            opinion = viewModel.getOpinionLiveData().value,
            scores = viewModel.getOpinionLiveData().value?.score?.let { listOf(it) } ?: emptyList(),
        ) { opinion ->
            viewModel.sendUpdateOpinion(opinion)
        }.show(
            this.parentFragmentManager,
            CreateEditOpinionDialog::class.java.simpleName
        )
    }

    private fun setCreateRecommendationDialog() {
        viewModel.getOpinionLiveData().value?.toGenericDomain()?.let {
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