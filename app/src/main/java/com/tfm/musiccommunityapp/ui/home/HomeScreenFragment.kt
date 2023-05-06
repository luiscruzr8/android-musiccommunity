package com.tfm.musiccommunityapp.ui.home

import androidx.fragment.app.Fragment
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.HomeScreenFragmentBinding
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeScreenFragment : Fragment(R.layout.home_screen_fragment) {

    private val binding by viewBinding(HomeScreenFragmentBinding::bind)

    private val viewModel by viewModel<HomeScreenViewModel>()

}