package com.tfm.musiccommunityapp.ui.community.events.detail

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
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.data.api.model.toGenericDomain
import com.tfm.musiccommunityapp.databinding.EventDetailFragmentBinding
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.ui.community.comments.CommentsAdapter
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.dialogs.community.CreateEditEventDialog
import com.tfm.musiccommunityapp.ui.dialogs.community.CreateEditRecommendationDialog
import com.tfm.musiccommunityapp.ui.dialogs.community.PostOrRespondCommentDialog
import com.tfm.musiccommunityapp.utils.formatDateTimeToString
import com.tfm.musiccommunityapp.utils.formatDateToString
import com.tfm.musiccommunityapp.utils.getChipColor
import com.tfm.musiccommunityapp.utils.getChipLabel
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventDetailFragment: BaseFragment(R.layout.event_detail_fragment) {

    private val binding by viewBinding(EventDetailFragmentBinding::bind)
    private val viewModel by viewModel<EventDetailViewModel>()
    private val args: EventDetailFragmentArgs by navArgs()

    private var cities = emptyList<CityDomain>()

    private val commentsAdapter = CommentsAdapter(
        ::onResponseComment,
        ::onDeleteComment
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventId = args.id
        viewModel.setUpEvent(eventId)

        observeLoader()
        observePostImageResult()
        observeEventResult()
        observeEventError()
        observeIsUserOwner()
        observeOperationSuccessful()
        observeCitiesResult()

        observeCommentsResult()
    }

    private fun observeLoader() {
        viewModel.isEventLoading().observe(viewLifecycleOwner) { showLoader ->
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

    private fun observeEventResult() {
        viewModel.getEventLiveData().observe(viewLifecycleOwner) {
            it?.let { event ->
                binding.apply {
                    //Hidden by default
                    tvTagsLabel.isVisible = false
                    relatedTagsLayout.isVisible = false

                    tvPostChip.chipBackgroundColor = getChipColor(event.postType, requireContext())
                    tvPostChip.text = String.format(
                        getString(R.string.chip_post),
                        event.id,
                        getChipLabel(event.postType, requireContext())
                    )

                    tvPostTitle.text = event.title
                    tvCreationDate.text = event.createdOn.formatDateToString()
                    tvCreationUser.text = event.login

                    tvLocation.text = event.cityName
                    tvStartAndEndDate.text = String.format(
                        getString(R.string.post_start_end_text),
                        event.from.formatDateTimeToString(),
                        event.to.formatDateTimeToString()
                    )

                    tvDescription.text = event.description
                    if (event.tags.isNotEmpty()) {
                        tvTagsLabel.isVisible = true
                        relatedTagsLayout.isVisible = true
                        relatedTagsLayout.setTagList(event.tags.map { it2 -> it2.tagName })
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
                        R.id.edit_option -> setEditEventDialog()
                        R.id.delete_option -> deleteEvent()
                        R.id.recommend_option -> setCreateRecommendationDialog()
                    }
                    true
                }
                popup.show()
            }
        }
    }

    private fun observeEventError() {
        viewModel.getEventByIdError().observe(viewLifecycleOwner) { error ->
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
                    EventDetailViewModel.EventOperationSuccess.DELETE ->
                        findNavController().popBackStack()

                    EventDetailViewModel.EventOperationSuccess.UPDATE ->
                        viewModel.setUpEvent(args.id)

                    EventDetailViewModel.EventOperationSuccess.RECOMMEND ->
                        findNavController().popBackStack()

                    EventDetailViewModel.EventOperationSuccess.COMMENT ->
                        viewModel.reloadPostComments(args.id)
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

    private fun deleteEvent() {
        viewModel.sendDeleteEvent()
    }

    private fun setEditEventDialog() {
        CreateEditEventDialog(
            event = viewModel.getEventLiveData().value,
            cities = cities,
        ) {
            viewModel.sendUpdateEvent(it)
        }.show(this.parentFragmentManager, CreateEditEventDialog::class.java.simpleName)
    }

    private fun setCreateRecommendationDialog() {
        viewModel.getEventLiveData().value?.toGenericDomain()?.let {
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