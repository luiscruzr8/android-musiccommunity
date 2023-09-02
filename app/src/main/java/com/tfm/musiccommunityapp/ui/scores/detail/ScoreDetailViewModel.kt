package com.tfm.musiccommunityapp.ui.scores.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserResult
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.opinion.CreateOpinionResult
import com.tfm.musiccommunityapp.domain.interactor.opinion.CreateOpinionUseCase
import com.tfm.musiccommunityapp.domain.interactor.score.DeleteScoreResult
import com.tfm.musiccommunityapp.domain.interactor.score.DeleteScoreUseCase
import com.tfm.musiccommunityapp.domain.interactor.score.GetScoreFileResult
import com.tfm.musiccommunityapp.domain.interactor.score.GetScoreFileUseCase
import com.tfm.musiccommunityapp.domain.interactor.score.GetScoreInfoByIdResult
import com.tfm.musiccommunityapp.domain.interactor.score.GetScoreInfoByIdUseCase
import com.tfm.musiccommunityapp.domain.model.OpinionDomain
import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.io.File

class ScoreDetailViewModel(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getScoreInfoById: GetScoreInfoByIdUseCase,
    private val getScoreFileById: GetScoreFileUseCase,
    private val deleteScore: DeleteScoreUseCase,
    private val createOpinion: CreateOpinionUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    enum class ScoreOperationSuccess { DELETE, OPINION }

    private val _scoreInfo: SingleLiveEvent<ScoreDomain?> = SingleLiveEvent()
    private val _scoreFile: SingleLiveEvent<File?> = SingleLiveEvent()
    private val _isOwnerUser: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _showScoreLoader: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _scoreInfoError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _scoreFileError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _isOperationSuccessful: SingleLiveEvent<Pair<ScoreOperationSuccess, Long>> =
        SingleLiveEvent()

    fun getScoreInfoLiveData() = _scoreInfo as LiveData<ScoreDomain?>
    fun getScoreFileLiveData() = _scoreFile as LiveData<File?>
    fun isOwnerUserLiveData() = _isOwnerUser as LiveData<Boolean>
    fun isScoreLoading() = _showScoreLoader as LiveData<Boolean>
    fun getScoreInfoError() = _scoreInfoError as LiveData<String>
    fun getScoreFileError() = _scoreFileError as LiveData<String>
    fun isOperationSuccessfulLiveData() =
        _isOperationSuccessful as LiveData<Pair<ScoreOperationSuccess, Long>>

    fun setUpScore(scoreId: Long) {
        viewModelScope.launch(dispatcher) {
            _showScoreLoader.postValue(true)
            handleGetScoreInfoResult(getScoreInfoById(scoreId), scoreId)
            handleGetCurrentUserResult(getCurrentUser())
        }
    }

    private suspend fun handleGetScoreInfoResult(result: GetScoreInfoByIdResult, scoreId: Long) {
        when (result) {
            is GetScoreInfoByIdResult.Success -> {
                _scoreInfo.postValue(result.score)
                handleGetScoreFileResult(getScoreFileById(scoreId))
            }

            is GetScoreInfoByIdResult.NoScore ->
                _scoreInfoError.postValue("Error code: 404 - Score not found")
        }
    }

    private fun handleGetScoreFileResult(result: GetScoreFileResult) {
        when (result) {
            is GetScoreFileResult.Success -> _scoreFile.postValue(result.scoreFile)

            is GetScoreFileResult.GenericError ->
                _scoreFileError.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is GetScoreFileResult.NetworkError ->
                _scoreFileError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
        _showScoreLoader.postValue(false)
    }

    private fun handleGetCurrentUserResult(result: GetCurrentUserResult) {
        when (result) {
            is GetCurrentUserResult.Success -> {
                _isOwnerUser.postValue(result.user?.let { _scoreInfo.value?.login == it.login }
                    ?: false)
            }

            GetCurrentUserResult.NoUser -> _isOwnerUser.postValue(false)
        }
    }

    fun sendDeleteScore() {
        viewModelScope.launch(dispatcher) {
            _showScoreLoader.postValue(true)
            handleDeleteScoreResult(deleteScore(_scoreInfo.value?.id ?: 0))
        }
    }

    private fun handleDeleteScoreResult(result: DeleteScoreResult) {
        when (result) {
            is DeleteScoreResult.Success -> {
                _isOperationSuccessful.postValue(ScoreOperationSuccess.DELETE to 0L)
            }

            is DeleteScoreResult.NetworkError ->
                _scoreInfoError.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is DeleteScoreResult.GenericError ->
                _scoreInfoError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
        _showScoreLoader.postValue(false)
    }

    fun sendCreateOpinion(opinion: OpinionDomain) {
        viewModelScope.launch(dispatcher) {
            _showScoreLoader.postValue(true)
            handleCreateOpinionResult(createOpinion(opinion))
        }
    }

    private fun handleCreateOpinionResult(result: CreateOpinionResult) {
        when (result) {
            is CreateOpinionResult.Success -> {
                _isOperationSuccessful.postValue(ScoreOperationSuccess.OPINION to result.id)
            }

            is CreateOpinionResult.NetworkError ->
                _scoreInfoError.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is CreateOpinionResult.GenericError ->
                _scoreInfoError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
        _showScoreLoader.postValue(false)
    }
}