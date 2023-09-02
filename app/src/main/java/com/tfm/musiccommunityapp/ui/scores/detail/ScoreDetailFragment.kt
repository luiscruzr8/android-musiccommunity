package com.tfm.musiccommunityapp.ui.scores.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.ScoreDetailFragmentBinding
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogTwoOptions
import com.tfm.musiccommunityapp.ui.dialogs.community.CreateEditOpinionDialog
import com.tfm.musiccommunityapp.utils.formatDateToString
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScoreDetailFragment: BaseFragment(R.layout.score_detail_fragment) {

    private val binding by viewBinding(ScoreDetailFragmentBinding::bind)
    private val viewModel by viewModel<ScoreDetailViewModel>()
    private val args: ScoreDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scoreId = args.id
        viewModel.setUpScore(scoreId)

        observeLoader()
        observeScoreInfoResult()
        observeScoreInfoError()
        observeScoreFileResult()
        observeScoreFileError()
        observeIsUserOwner()
        observeOperationSuccessful()
    }

    private fun observeLoader() {
        viewModel.isScoreLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeScoreInfoResult() {
        viewModel.getScoreInfoLiveData().observe(viewLifecycleOwner) { scoreInfo ->
            scoreInfo?.let {
                binding.tvScoreName.text = scoreInfo.name
                binding.tvScoreChip.text = getString(R.string.chip_score, scoreInfo.id)
                binding.tvCreatedByOn.text = getString(
                    R.string.scores_screen_created_by_on,
                    scoreInfo.login,
                    scoreInfo.uploadedOn.formatDateToString()
                )
            }
        }
    }

    private fun observeScoreFileResult() {
        viewModel.getScoreFileLiveData().observe(viewLifecycleOwner) { scoreFile ->
            scoreFile?.let {
                binding.scoreFilePdfView
                    .fromFile(it)
                    .enableSwipe(true)
                    .swipeHorizontal(true)
                    .enableAntialiasing(true)
                    .pageFitPolicy(FitPolicy.BOTH)
                    .pageFling(true)
                    .load()
            }
        }
    }

    private fun observeScoreInfoError() {
        viewModel.getScoreInfoError().observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                alertDialogOneOption(
                    requireContext(),
                    it,
                    null,
                    errorMessage,
                    getString(R.string.accept)
                ) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun observeScoreFileError() {
        viewModel.getScoreFileError().observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                alertDialogOneOption(
                    requireContext(),
                    it,
                    null,
                    errorMessage,
                    getString(R.string.accept)
                ) { }
            }
        }
    }

    private fun observeOperationSuccessful() {
        viewModel.isOperationSuccessfulLiveData().observe(viewLifecycleOwner) {
            it?.let { type ->
                when (type.first) {
                    ScoreDetailViewModel.ScoreOperationSuccess.DELETE ->
                        findNavController().popBackStack()

                    ScoreDetailViewModel.ScoreOperationSuccess.OPINION ->
                        alertDialogTwoOptions(
                            requireContext(),
                            getString(R.string.community_opinion_created_title),
                            null,
                            getString(R.string.community_opinion_created_message),
                            getString(R.string.ok),
                            {
                                val action = ScoreDetailFragmentDirections
                                    .actionScoreDetailFragmentToOpinionDetailFragment(
                                        type.second
                                    )
                                navigateSafe(action)
                            },
                            getString(R.string.not_now_button)
                        ) { }
                }
            }
        }
    }

    private fun observeIsUserOwner() {
        viewModel.isOwnerUserLiveData().observe(viewLifecycleOwner) { isOwner ->
            binding.apply {
                ivDeleteButton.isVisible = isOwner
                ivCreateOpinion.isVisible = isOwner

                ivDeleteButton.setOnClickListener {
                    if (isOwner) {
                        deleteScore()
                    }
                }
                ivCreateOpinion.setOnClickListener {
                    if (isOwner) {
                        setCreateOpinionDialog()
                    }
                }
            }
        }
    }

    private fun deleteScore() {
        viewModel.sendDeleteScore()
    }

    private fun setCreateOpinionDialog() {
        viewModel.getScoreInfoLiveData().value?.let { scoreInfo ->
            CreateEditOpinionDialog(
                null,
                listOf(scoreInfo)
            ) { opinion ->
                alertDialogTwoOptions(
                    requireContext(),
                    getString(R.string.user_alert),
                    null,
                    getString(R.string.create_opinion_confirmation),
                    getString(R.string.accept),
                    {
                        viewModel.sendCreateOpinion(opinion)
                    },
                    getString(R.string.cancel)
                ) { }
            }.show(
                this.parentFragmentManager,
                CreateEditOpinionDialog::class.java.simpleName
            )
        }
    }

}