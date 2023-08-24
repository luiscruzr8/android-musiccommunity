package com.tfm.musiccommunityapp.ui.community.events

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.EventsFragmentBinding
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.ui.community.CommunityFragmentDirections
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventsFragment: BaseFragment(R.layout.events_fragment) {

    private val binding by viewBinding(EventsFragmentBinding::bind)
    private val viewModel by viewModel<EventsViewModel>()
    private val eventsAdapter = EventsAdapter(::onEventClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoader()
        observeEventsResult()
        observeEventsError()

        binding.rvCommunityEvents.apply {
            adapter = eventsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    fun restartViewPager(username: String?) {
        viewModel.setUpEvents(if (username.isNullOrBlank()) null else username)
    }

    private fun observeLoader() {
        viewModel.isEventsLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeEventsResult() {
        viewModel.getEventsLiveData().observe(viewLifecycleOwner) { events ->
            eventsAdapter.setEvents(events)
            binding.noEventsFound.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun onEventClicked(event: EventDomain) {
        val action =
            CommunityFragmentDirections.actionCommunityFragmentToEventDetailFragment(event.id)
        navigateSafe(action)
    }

    private fun observeEventsError() {
        viewModel.getCommunityEventsError().observe(viewLifecycleOwner) { error ->
            binding.noEventsFound.text = error
        }
    }
}