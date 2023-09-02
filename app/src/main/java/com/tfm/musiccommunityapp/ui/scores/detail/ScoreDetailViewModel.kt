package com.tfm.musiccommunityapp.ui.scores.detail

import androidx.lifecycle.ViewModel
import com.tfm.musiccommunityapp.domain.interactor.score.GetScoreFileUseCase
import com.tfm.musiccommunityapp.domain.interactor.score.GetScoreInfoByIdUseCase
import kotlinx.coroutines.CoroutineDispatcher

class ScoreDetailViewModel(
    private val getScoreInfoById: GetScoreInfoByIdUseCase,
    private val getScoreFile: GetScoreFileUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {
}