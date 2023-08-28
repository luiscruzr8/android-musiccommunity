package com.tfm.musiccommunityapp.ui.community

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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
import com.tfm.musiccommunityapp.ui.community.users.UsersFragment
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommunityFragment : BaseFragment(R.layout.community_fragment) {

    private val binding by viewBinding(CommunityFragmentBinding::bind)
    private val viewModel by viewModel<CommunityViewModel>()

    private var searchTerm = ""
    private var cities = emptyList<CityDomain>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCH_TERM, binding.searchEditText.text)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setUpCities()
        observeCitiesResult()

        val adapter = setUpAdapter()
        binding.communityViewPager.adapter = adapter

        if (savedInstanceState != null) {
            searchTerm = savedInstanceState.getCharSequence(SEARCH_TERM, "").toString()
            binding.searchEditText.setText(searchTerm)
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Search logic here.
                searchTerm = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.materialButton.setOnClickListener {
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
        adapter.addFragment(UsersFragment(), getString(R.string.community_tab_users), resources.getDrawable(R.drawable.ic_tab_users))
        adapter.addFragment(AdvertisementsFragment(), getString(R.string.community_tab_advertisements), resources.getDrawable(R.drawable.ic_tab_advertisement))
        adapter.addFragment(EventsFragment(), getString(R.string.community_tab_events), resources.getDrawable(R.drawable.ic_tab_events))
        adapter.addFragment(DiscussionsFragment(), getString(R.string.community_tab_discussions), resources.getDrawable(R.drawable.ic_tab_discussions))
        adapter.addFragment(OpinionsFragment(), getString(R.string.community_tab_opinions), resources.getDrawable(R.drawable.ic_tab_opinions))
        //adapter.addFragment(RatingsFragment(), getString(R.string.community_tab_recommendations), resources.getDrawable(R.drawable.ic_tab_recommendations))
        return adapter
    }

    private fun setUpTab(fragment: Fragment) {
        when (fragment) {
            is UsersFragment -> {
                fragment.restartViewPager(searchTerm)
                binding.fabAddItem.isVisible = false
            }

            is EventsFragment -> {
                fragment.restartViewPager(searchTerm)
                binding.fabAddItem.isVisible = true
            }

            is AdvertisementsFragment -> {
                fragment.restartViewPager(searchTerm)
                binding.fabAddItem.isVisible = true
            }

            is DiscussionsFragment -> {
                fragment.restartViewPager(searchTerm)
                binding.fabAddItem.isVisible = true
            }

            is OpinionsFragment -> {
                fragment.restartViewPager(searchTerm)
                binding.fabAddItem.isVisible = true
            }
            /*is RatingsFragment -> {
                fragment.restartViewPager(searchTerm)
                binding.fabAddItem.isVisible = true
            }*/
            else -> {}
        }
    }

    private fun observeCitiesResult() {
        viewModel.getCitiesLiveData().observe(viewLifecycleOwner) { cityList ->
            cities = cityList
        }
    }

    companion object {
        const val SEARCH_TERM = "searchTerm"
    }
}