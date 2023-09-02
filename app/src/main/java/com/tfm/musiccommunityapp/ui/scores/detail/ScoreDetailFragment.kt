package com.tfm.musiccommunityapp.ui.scores.detail

import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.ScoreDetailFragmentBinding
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScoreDetailFragment: BaseFragment(R.layout.score_detail_fragment) {

    private val binding by viewBinding(ScoreDetailFragmentBinding::bind)
    private val viewModel by viewModel<ScoreDetailViewModel>()

}