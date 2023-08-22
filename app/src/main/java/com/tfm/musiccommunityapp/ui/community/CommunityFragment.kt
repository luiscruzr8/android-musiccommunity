package com.tfm.musiccommunityapp.ui.community

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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

        childFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                super.onFragmentResumed(fm, f)
                when (f) {
                    is UsersFragment -> f.restartViewPager()
                    /*is AdvertisementsFragment -> f.restartViewPager()
                    is EventsFragment -> f.restartViewPager()
                    is DiscussionsFragment -> f.restartViewPager()
                    is OpinionsFragment -> f.restartViewPager()
                    is RatingsFragment -> f.restartViewPager()*/
                    else -> {}
                }
            }
        }, false)
        
    }

    private fun setUpAdapter():ViewPagerAdapter {
        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(
            UsersFragment(),
            getString(R.string.community_tab_users),
            resources.getDrawable(R.drawable.ic_tab_users)
        )
        adapter.addFragment(
            AdvertisementsFragment(),
            getString(R.string.community_tab_advertisements),
            resources.getDrawable(R.drawable.ic_tab_advertisement)
        )
        adapter.addFragment(
            EventsFragment(),
            getString(R.string.community_tab_events),
            resources.getDrawable(R.drawable.ic_tab_events)
        )
        adapter.addFragment(
            DiscussionsFragment(),
            getString(R.string.community_tab_discussions),
            resources.getDrawable(R.drawable.ic_tab_discussions)
        )
        adapter.addFragment(
            OpinionsFragment(),
            getString(R.string.community_tab_opinions),
            resources.getDrawable(R.drawable.ic_tab_opinions)
        )
        //adapter.addFragment(RatingsFragment(), getString(R.string.community_tab_recommendations), resources.getDrawable(R.drawable.ic_tab_recommendations))
        return adapter
    }
}