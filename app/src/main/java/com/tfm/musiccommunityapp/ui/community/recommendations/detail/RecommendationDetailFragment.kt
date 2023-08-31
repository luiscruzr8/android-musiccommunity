package com.tfm.musiccommunityapp.ui.community.recommendations.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.RecommendationDetailFragmentBinding
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.dialogs.community.CreateEditRecommendationDialog
import com.tfm.musiccommunityapp.ui.dialogs.rating.RatingDialog
import com.tfm.musiccommunityapp.utils.formatDateToString
import com.tfm.musiccommunityapp.utils.getChipColor
import com.tfm.musiccommunityapp.utils.getChipLabel
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecommendationDetailFragment: BaseFragment(R.layout.recommendation_detail_fragment) {

    private val binding by viewBinding(RecommendationDetailFragmentBinding::bind)
    private val viewModel by viewModel<RecommendationDetailViewModel>()
    private val args: RecommendationDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recommendationId = args.id
        viewModel.setUpRecommendation(recommendationId)

        observeLoader()
        observerPostImageResult()
        observeRecommendationResult()
        observeRecommendationError()
        observeIsUserOwner()
        observeOperationSuccessful()
    }

    private fun observeLoader() {
        viewModel.isRecommendationLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observerPostImageResult() {
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

    private fun observeRecommendationResult() {
        viewModel.getRecommendationLiveData().observe(viewLifecycleOwner) {
            it?.let { recommendation ->
                binding.apply {
                    tvRecommendationChip.text = String.format(
                        getString(R.string.chip_post),
                        recommendation.id,
                        getChipLabel(getString(R.string.recommendation), requireContext())
                    )
                    tvRecommendationTitle.text = recommendation.recommendationTitle
                    tvRecMessage.text = recommendation.recommendationMessage
                    tvRecCreationUser.text = recommendation.login
                    tvRecCreationDate.text = recommendation.createdOn.formatDateToString()
                    tvRecommendationRating.text =
                        String.format(getString(R.string.chip_rating), recommendation.rating)

                    with(recommendation.post.postType) {
                        binding.tvPostChip.chipBackgroundColor =
                            getChipColor(this, requireContext())
                        tvPostChip.text = String.format(
                            getString(R.string.chip_post),
                            recommendation.postId,
                            getChipLabel(this, requireContext())
                        )
                    }
                    tvPostTitle.text = recommendation.post.title
                    tvPostCreationUser.text = recommendation.post.login
                    tvPostCreationDate.text = recommendation.post.createdOn.formatDateToString()
                    tvPostDescription.text = recommendation.post.description
                    if (recommendation.post.tags.isNotEmpty()) {
                        tvTagsLabel.isVisible = true
                        relatedTagsLayout.isVisible = true
                        relatedTagsLayout.setTagList(recommendation.post.tags.map { it2 -> it2.tagName })
                    }
                }
            }
        }
    }

    private fun observeIsUserOwner() {
        viewModel.isUserOwnerLiveData().observe(viewLifecycleOwner) { isOwner ->
            binding.optionsButton.setOnClickListener {
                val popup = PopupMenu(requireContext(), binding.optionsButton)
                popup.menuInflater.inflate(R.menu.popup_recommendation_options, popup.menu)

                if (!isOwner) {
                    popup.menu.removeItem(R.id.recommendation_edit_option)
                    popup.menu.removeItem(R.id.recommendation_delete_option)
                } else {
                    popup.menu.removeItem(R.id.recommendation_rate_option)
                }

                popup.setOnMenuItemClickListener { item: MenuItem ->
                    when (item.itemId) {
                        R.id.recommendation_edit_option -> setEditRecommendationDialog()
                        R.id.recommendation_delete_option -> deleteRecommendation()
                        R.id.recommendation_rate_option -> setRatingDialog()
                    }
                    true
                }
                popup.show()
            }
        }
    }

    private fun observeRecommendationError() {
        viewModel.getRecommendationByIdError().observe(viewLifecycleOwner) { error ->
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
        viewModel.getOperationSuccessfulLiveData().observe(viewLifecycleOwner) { type ->
            type?.let {
                when (type) {
                    RecommendationDetailViewModel.RecommendationOperationSuccess.DELETE ->
                        findNavController().popBackStack()
                    RecommendationDetailViewModel.RecommendationOperationSuccess.UPDATE ->
                        viewModel.setUpRecommendation(args.id)
                    RecommendationDetailViewModel.RecommendationOperationSuccess.RATE ->
                        viewModel.setUpRecommendation(args.id)
                }
            }
        }
    }

    private fun deleteRecommendation() {
        viewModel.sendDeleteRecommendation()
    }

    private fun setEditRecommendationDialog() {
        viewModel.getRecommendationLiveData().value?.let {
            CreateEditRecommendationDialog(
                recommendation = it,
                post = it.post,
            ) { recommendation ->
                viewModel.sendUpdateRecommendation(recommendation)
            }.show(
                this.parentFragmentManager,
                CreateEditRecommendationDialog::class.java.simpleName
            )
        }
    }

    private fun setRatingDialog() {
        viewModel.getRecommendationLiveData().value?.let {
            RatingDialog(
                recommendationId = it.id,
                onSaveClicked = { id, rating ->
                    viewModel.sendRateRecommendation(id, rating)
                }
            ).show(this.parentFragmentManager, RatingDialog::class.java.simpleName)
        }
    }

}