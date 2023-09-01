package com.tfm.musiccommunityapp.ui.community

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayoutMediator
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.CommunityFragmentBinding
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.ui.community.advertisements.AdvertisementsFragment
import com.tfm.musiccommunityapp.ui.community.discussions.DiscussionsFragment
import com.tfm.musiccommunityapp.ui.community.events.EventsFragment
import com.tfm.musiccommunityapp.ui.community.opinions.OpinionsFragment
import com.tfm.musiccommunityapp.ui.community.recommendations.RecommendationsFragment
import com.tfm.musiccommunityapp.ui.community.users.UsersFragment
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.dialogs.community.CreateEditAdvertisementDialog
import com.tfm.musiccommunityapp.ui.dialogs.community.CreateEditDiscussionDialog
import com.tfm.musiccommunityapp.ui.dialogs.community.CreateEditEventDialog
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommunityFragment : BaseFragment(R.layout.community_fragment) {

    private val binding by viewBinding(CommunityFragmentBinding::bind)
    private val viewModel by viewModel<CommunityViewModel>()

    private var searchTerm = ""
    private var isTop10Selected = false
    private var cities = emptyList<CityDomain>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCH_TERM, binding.searchEditText.text)
        outState.putBoolean(IS_TOP_TEN_SELECTED, binding.isTop10CheckBox.isChecked)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setUpCities()
        observeCitiesResult()
        observeOperationResult()
        observeOperationErrors()

        val adapter = setUpAdapter()
        binding.communityViewPager.adapter = adapter

        if (savedInstanceState != null) {
            searchTerm = savedInstanceState.getCharSequence(SEARCH_TERM, "").toString()
            isTop10Selected = savedInstanceState.getBoolean(IS_TOP_TEN_SELECTED, false)
            binding.searchEditText.setText(searchTerm)
            binding.isTop10CheckBox.isChecked = isTop10Selected
        }

        binding.isTop10CheckBox.setOnCheckedChangeListener { _, isChecked ->
            isTop10Selected = isChecked
            setUpTab(adapter.getFragmentOnPosition(binding.communityViewPager.currentItem))
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchTerm = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.submitSearchButton.setOnClickListener {
            setUpTab(adapter.getFragmentOnPosition(binding.communityViewPager.currentItem))
        }

        TabLayoutMediator(binding.tabLayout, binding.communityViewPager) { tab, position ->
            tab.text = setUpAdapter().getPageTitle(position)
            tab.icon = setUpAdapter().getPageIcon(position)
        }.attach()

        childFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                super.onFragmentResumed(fm, f)
                setUpTab(f)
            }
        }, false)
        
    }

    private fun setUpAdapter():ViewPagerAdapter {
        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(
            UsersFragment(),
            getString(R.string.community_tab_users),
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_users)
        )
        adapter.addFragment(
            AdvertisementsFragment(),
            getString(R.string.community_tab_advertisements),
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_advertisement)
        )
        adapter.addFragment(
            EventsFragment(),
            getString(R.string.community_tab_events),
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_events)
        )
        adapter.addFragment(
            DiscussionsFragment(),
            getString(R.string.community_tab_discussions),
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_discussions)
        )
        adapter.addFragment(
            OpinionsFragment(),
            getString(R.string.community_tab_opinions),
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_opinions)
        )
        adapter.addFragment(
            RecommendationsFragment(),
            getString(R.string.community_tab_recommendations),
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_tab_recommendations)
        )
        return adapter
    }

    private fun setUpTab(fragment: Fragment) {
        when (fragment) {
            is UsersFragment -> {
                fragment.restartViewPager(searchTerm)
                binding.fabAddItem.isVisible = false
                binding.isTop10CheckBox.isVisible = false
            }

            is EventsFragment -> {
                fragment.restartViewPager(searchTerm)
                binding.fabAddItem.isVisible = true
                binding.isTop10CheckBox.isVisible = false
                setCreateEventDialog()
            }

            is AdvertisementsFragment -> {
                fragment.restartViewPager(searchTerm)
                binding.fabAddItem.isVisible = true
                binding.isTop10CheckBox.isVisible = false
                setCreateAdvertisementDialog()
            }

            is DiscussionsFragment -> {
                fragment.restartViewPager(searchTerm)
                binding.fabAddItem.isVisible = true
                binding.isTop10CheckBox.isVisible = false
                setCreateDiscussionDialog()
            }

            is OpinionsFragment -> {
                fragment.restartViewPager(searchTerm)
                binding.fabAddItem.isVisible = true
                binding.isTop10CheckBox.isVisible = false
            }

            is RecommendationsFragment -> {
                fragment.restartViewPager(isTop10Selected, searchTerm)
                binding.fabAddItem.isVisible = false
                binding.isTop10CheckBox.isVisible = true
            }

            else -> {}
        }
    }

    private fun observeCitiesResult() {
        viewModel.getCitiesLiveData().observe(viewLifecycleOwner) { cityList ->
            cities = cityList
        }
    }

    private fun observeOperationResult() {
        viewModel.getOperationResult().observe(viewLifecycleOwner) { operation ->
            when (operation.first) {
                CommunityViewModel.OperationSuccess.CREATE_EVENT_SUCCESS -> {
                    alertDialogOneOption(
                        requireContext(),
                        getString(R.string.community_event_created_title),
                        null,
                        getString(R.string.community_event_created_message),
                        getString(R.string.ok)
                    ) {
                        val action = CommunityFragmentDirections.actionCommunityFragmentToEventDetailFragment(operation.second)
                        navigateSafe(action)
                    }
                }
                CommunityViewModel.OperationSuccess.CREATE_ADVERTISEMENT_SUCCESS -> {
                    alertDialogOneOption(
                        requireContext(),
                        getString(R.string.community_advertisement_created_title),
                        null,
                        getString(R.string.community_advertisement_created_message),
                        getString(R.string.ok)
                    ) {
                        val action = CommunityFragmentDirections.actionCommunityFragmentToAdvertisementDetailFragment(operation.second)
                        navigateSafe(action)
                    }
                }
                CommunityViewModel.OperationSuccess.CREATE_DISCUSSION_SUCCESS -> {
                    alertDialogOneOption(
                        requireContext(),
                        getString(R.string.community_discussion_created_title),
                        null,
                        getString(R.string.community_discussion_created_message),
                        getString(R.string.ok)
                    ) {
                        val action = CommunityFragmentDirections.actionCommunityFragmentToDiscussionDetailFragment(operation.second)
                        navigateSafe(action)
                    }
                }
            }
        }
    }

    private fun observeOperationErrors() {
        viewModel.getCreateItemError().observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
        }
    }

    private fun setCreateEventDialog() {
        binding.fabAddItem.setOnClickListener {
            CreateEditEventDialog(
                event = null,
                cities = cities,
            ) {
                viewModel.sendCreateEvent(it)
            }.show(this.parentFragmentManager, CreateEditEventDialog::class.java.simpleName)
        }
    }

    private fun setCreateAdvertisementDialog() {
        binding.fabAddItem.setOnClickListener {
            CreateEditAdvertisementDialog(
                advertisement = null,
                cities = cities,
            ) {
                viewModel.sendCreateAdvertisement(it)
            }.show(this.parentFragmentManager, CreateEditAdvertisementDialog::class.java.simpleName)
        }
    }

    private fun setCreateDiscussionDialog() {
        binding.fabAddItem.setOnClickListener {
            CreateEditDiscussionDialog(
                    discussion = null,
            ) {
                viewModel.sendCreateDiscussion(it)
            }.show(this.parentFragmentManager, CreateEditDiscussionDialog::class.java.simpleName)
        }
    }

    companion object {
        const val SEARCH_TERM = "searchTerm"
        const val IS_TOP_TEN_SELECTED = "isTop10Selected"
    }
}