package com.tfm.musiccommunityapp.ui.community

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.CommunityFragmentBinding
import com.tfm.musiccommunityapp.ui.community.advertisements.AdvertisementsFragment
import com.tfm.musiccommunityapp.ui.community.discussions.DiscussionsFragment
import com.tfm.musiccommunityapp.ui.community.events.EventsFragment
import com.tfm.musiccommunityapp.ui.community.opinions.OpinionsFragment
import com.tfm.musiccommunityapp.ui.community.users.UsersFragment
import com.tfm.musiccommunityapp.utils.viewBinding

class CommunityFragment : BaseFragment(R.layout.community_fragment) {

    private val binding by viewBinding(CommunityFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = setUpAdapter()
        binding.communityViewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.communityViewPager) { tab, position ->
            tab.text = setUpAdapter().getPageTitle(position)
            tab.icon = setUpAdapter().getPageIcon(position)
        }.attach()

        binding.communityViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(val currentFragment = adapter.getFragmentOnPosition(position)){
                    is UsersFragment -> currentFragment.restartViewPager()
                    /*is AdvertisementsFragment -> currentFragment..restartViewPager()
                    is EventsFragment -> currentFragment..restartViewPager()
                    is DiscussionsFragment -> currentFragment..restartViewPager()
                    is OpinionsFragment -> currentFragment..restartViewPager()
                    is RatingsFragment -> currentFragment..restartViewPager()*/
                    else -> {}
                }
            }
        })
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
}