package com.tfm.musiccommunityapp.ui.profile

import android.os.Bundle
import android.view.View
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.ProfileFragmentBinding
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.utils.viewBinding

class ProfileFragment : BaseFragment(R.layout.profile_fragment) {

    private val binding by viewBinding(ProfileFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}