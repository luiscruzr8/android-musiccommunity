package com.tfm.musiccommunityapp.ui.community.events.detail

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
import com.tfm.musiccommunityapp.databinding.EventDetailFragmentBinding
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.dialogs.community.CreateEditEventDialog
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
                        R.id.recommend_option -> {}
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

                    EventDetailViewModel.EventOperationSuccess.RECOMMEND -> {
                        //TODO
                    }
                }
            }
        }
    }

    private fun observeCitiesResult() {
        viewModel.getCitiesLiveData().observe(viewLifecycleOwner) { cityList ->
            cities = cityList
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

}