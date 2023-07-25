package com.tfm.musiccommunityapp.ui.home

import android.os.Bundle
import android.view.View
import com.tfm.musiccommunityapp.BuildConfig
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.ShowMoreInfoFragmentBinding
import com.tfm.musiccommunityapp.utils.viewBinding

class ShowMoreInfoFragment: BaseFragment(R.layout.show_more_info_fragment) {

    private val binding by viewBinding(ShowMoreInfoFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.versionFooter.text = getString(R.string.version, BuildConfig.VERSION_NAME)
    }


}