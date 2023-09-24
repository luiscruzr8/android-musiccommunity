package com.tfm.musiccommunityapp.ui.community

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private val fragmentList = ArrayList<Fragment>()
    private val tabTitleList = ArrayList<String>()
    private val tabIconList = ArrayList<Drawable?>()

    fun addFragment(fragment: Fragment, title: String, icon: Drawable?) {
        fragmentList.add(fragment)
        tabTitleList.add(title)
        tabIconList.add(icon)
    }

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun getPageTitle(position: Int): CharSequence = tabTitleList[position]

    fun getPageIcon(position: Int): Drawable? = tabIconList[position]

    fun getFragmentOnPosition(position: Int): Fragment = fragmentList[position]
}