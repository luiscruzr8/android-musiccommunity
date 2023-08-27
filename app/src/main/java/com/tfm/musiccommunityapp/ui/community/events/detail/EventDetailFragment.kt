package com.tfm.musiccommunityapp.ui.community.events.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.EventDetailFragmentBinding
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.utils.formatDateTimeToString
import com.tfm.musiccommunityapp.utils.getChipColor
import com.tfm.musiccommunityapp.utils.getChipLabel
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventDetailFragment: BaseFragment(R.layout.event_detail_fragment) {

    private val binding by viewBinding(EventDetailFragmentBinding::bind)
    private val viewModel by viewModel<EventDetailViewModel>()
    private val args: EventDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventId = args.id
        viewModel.setUpEvent(eventId)

        observeLoader()
        observePostImageResult()
        observeEventResult()
        observeEventError()
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
        viewModel.getEventLiveData().observe(viewLifecycleOwner) { it ->
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
                    tvCreationDate.text = event.createdOn.formatDateTimeToString()
                    tvCreationUser.text = event.login

                    tvEventLocation.text = event.cityName
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
                }
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
}