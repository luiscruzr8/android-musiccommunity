package com.tfm.musiccommunityapp.ui.community.advertisements.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.api.model.toGenericDomain
import com.tfm.musiccommunityapp.databinding.AdvertisementDetailFragmentBinding
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.ui.base.BaseFragment
import com.tfm.musiccommunityapp.ui.community.comments.CommentsAdapter
import com.tfm.musiccommunityapp.ui.di.GlideApp
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.dialogs.community.CreateEditAdvertisementDialog
import com.tfm.musiccommunityapp.ui.dialogs.community.CreateEditRecommendationDialog
import com.tfm.musiccommunityapp.ui.dialogs.community.PostOrRespondCommentDialog
import com.tfm.musiccommunityapp.ui.utils.formatDateToString
import com.tfm.musiccommunityapp.ui.utils.getChipColor
import com.tfm.musiccommunityapp.ui.utils.getChipLabel
import com.tfm.musiccommunityapp.ui.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdvertisementDetailFragment: BaseFragment(R.layout.advertisement_detail_fragment) {

    private val binding by viewBinding(AdvertisementDetailFragmentBinding::bind)
    private val viewModel by viewModel<AdvertisementDetailViewModel>()
    private val args: AdvertisementDetailFragmentArgs by navArgs()

    private val commentsAdapter = CommentsAdapter(
        ::onResponseComment,
        ::onDeleteComment
    )

    private var cities = emptyList<CityDomain>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val advertisementId = args.id
        viewModel.setUpAdvertisement(advertisementId)

        observeLoader()
        observePostImageResult()
        observeAdvertisementResult()
        observeAdvertisementError()
        observeIsUserOwner()
        observeOperationSuccessful()
        observeCitiesResult()
        observeCommentsResult()
    }

    private fun observeLoader() {
        viewModel.isAdvertisementLoading().observe(viewLifecycleOwner) { showLoader ->
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
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.ivPostImage)
            }
        }
    }

    private fun observeAdvertisementResult() {
        viewModel.getAdvertisementLiveData().observe(viewLifecycleOwner) { it ->
            it?.let { advertisement ->
                binding.apply {
                    //Hidden by default
                    tvTagsLabel.isVisible = false
                    relatedTagsLayout.isVisible = false

                    tvPostChip.chipBackgroundColor =
                        getChipColor(advertisement.postType, requireContext())
                    tvPostChip.text = String.format(
                        getString(R.string.chip_post),
                        advertisement.id,
                        getChipLabel(advertisement.postType, requireContext())
                    )

                    tvPostTitle.text = advertisement.title
                    tvCreationDate.text = advertisement.createdOn.formatDateToString()
                    tvCreationUser.text = advertisement.login

                    tvLocation.text = advertisement.cityName
                    tvUntilDate.text = String.format(
                        getString(R.string.post_end_text),
                        advertisement.until.formatDateToString()
                    )

                    tvDescription.text = advertisement.description
                    if (advertisement.tags.isNotEmpty()) {
                        tvTagsLabel.isVisible = true
                        relatedTagsLayout.isVisible = true
                        relatedTagsLayout.setTagList(advertisement.tags.map { it2 -> it2.tagName })
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
                        R.id.edit_option -> setEditAdvertisementDialog()
                        R.id.delete_option -> deleteAdvertisement()
                        R.id.recommend_option -> setCreateRecommendationDialog()
                    }
                    true
                }
                popup.show()
            }
        }
    }

    private fun observeAdvertisementError() {
        viewModel.getAdvertisementByIdError().observe(viewLifecycleOwner) { error ->
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
                    AdvertisementDetailViewModel.AdvertisementOperationSuccess.DELETE ->
                        findNavController().popBackStack()

                    AdvertisementDetailViewModel.AdvertisementOperationSuccess.UPDATE ->
                        viewModel.setUpAdvertisement(args.id)

                    AdvertisementDetailViewModel.AdvertisementOperationSuccess.RECOMMEND ->
                        findNavController().popBackStack()

                    AdvertisementDetailViewModel.AdvertisementOperationSuccess.COMMENT ->
                        viewModel.reloadPostComments(args.id)

                    AdvertisementDetailViewModel.AdvertisementOperationSuccess.IMAGE_UPLOAD ->
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.image_uploaded_successfully),
                            Toast.LENGTH_LONG
                        ).show()
                }
            }
        }
    }

    private fun observeCitiesResult() {
        viewModel.getCitiesLiveData().observe(viewLifecycleOwner) { cityList ->
            cities = cityList
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

    private fun deleteAdvertisement() {
        viewModel.sendDeleteAdvertisement()
    }

    private fun setEditAdvertisementDialog() {
        CreateEditAdvertisementDialog(
            advertisement = viewModel.getAdvertisementLiveData().value,
            cities = cities,
        ) {
            viewModel.sendUpdateAdvertisement(it)
        }.show(this.parentFragmentManager, CreateEditAdvertisementDialog::class.java.simpleName)
    }

    private fun setCreateRecommendationDialog() {
        viewModel.getAdvertisementLiveData().value?.toGenericDomain()?.let {
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