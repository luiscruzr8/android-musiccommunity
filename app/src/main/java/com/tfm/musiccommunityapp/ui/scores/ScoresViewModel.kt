package com.tfm.musiccommunityapp.ui.scores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserResult
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.score.GetUserScoresResult
import com.tfm.musiccommunityapp.domain.interactor.score.GetUserScoresUseCase
import com.tfm.musiccommunityapp.domain.interactor.score.UploadScoreResult
import com.tfm.musiccommunityapp.domain.interactor.score.UploadScoreUseCase
import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.io.File

class ScoresViewModel(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getPrivateUserScores: GetUserScoresUseCase,
    private val getPublicUserScores: GetUserScoresUseCase,
    private val uploadScore: UploadScoreUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    enum class ScoreOperationSuccess { UPLOAD, DELETE }

    private val _scores: MutableLiveData<List<ScoreDomain>> = MutableLiveData()
    private val _scoresError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _scoreUploadError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showScoresLoader = MutableLiveData<Boolean>()
    private val _isOperationSuccessful: SingleLiveEvent<ScoreOperationSuccess> = SingleLiveEvent()

    fun getScoresLiveData() = _scores as LiveData<List<ScoreDomain>>
    fun getScoresError() = _scoresError as LiveData<String>
    fun getScoreUploadError() = _scoreUploadError as LiveData<String>
    fun isScoresLoading() = _showScoresLoader as LiveData<Boolean>
    fun isOperationSuccessfulLiveData() = _isOperationSuccessful as LiveData<ScoreOperationSuccess>

    fun setUpScores(searchTerm: String?, login: String? = null) {
        viewModelScope.launch(dispatcher) {
            _showScoresLoader.postValue(true)
            login?.let {
                handleGetUserScoresResult(getPublicUserScores(login, searchTerm, true))
            } ?: run {
                handleCurrentUser(searchTerm, getCurrentUser())
            }
        }
    }

    private suspend fun handleCurrentUser(searchTerm: String?, result: GetCurrentUserResult) {
        when (result) {
            is GetCurrentUserResult.Success ->
                handleGetMyScoresResult(getPrivateUserScores(result.user?.login, searchTerm, false))

            is GetCurrentUserResult.NoUser -> sendScoresError(404, "User not found")
        }
    }

    private fun handleGetMyScoresResult(result: GetUserScoresResult) {
        when (result) {
            is GetUserScoresResult.Success -> {
                _scores.postValue(result.scores)
            }

            is GetUserScoresResult.NetworkError -> {
                _scores.postValue(emptyList())
                sendScoresError(result.error.code, result.error.message)
            }

            is GetUserScoresResult.GenericError -> {
                _scores.postValue(emptyList())
                sendScoresError(result.error.code, result.error.message)
            }
        }
        _showScoresLoader.postValue(false)
    }

    private fun handleGetUserScoresResult(result: GetUserScoresResult) {
        when (result) {
            is GetUserScoresResult.Success -> {
                _scores.postValue(result.scores)
            }

            is GetUserScoresResult.NetworkError -> {
                _scores.postValue(emptyList())
                sendScoresError(result.error.code, result.error.message)
            }

            is GetUserScoresResult.GenericError -> {
                _scores.postValue(emptyList())
                sendScoresError(result.error.code, result.error.message)
            }
        }
        _showScoresLoader.postValue(false)
    }

    private fun sendScoresError(code: Int, message: String) {
        _scoresError.postValue("Error code: $code - $message")
    }

    fun sendUploadScore(score: File) {
        viewModelScope.launch(dispatcher) {
            _showScoresLoader.postValue(true)
            handleUploadScoreResult(uploadScore(score))
        }
    }

    private fun handleUploadScoreResult(result: UploadScoreResult) {
        when (result) {
            is UploadScoreResult.Success -> {
                _isOperationSuccessful.postValue(ScoreOperationSuccess.UPLOAD)
            }

            is UploadScoreResult.NetworkError -> {
                sendScoreUploadError(result.error.code, result.error.message)
            }

            is UploadScoreResult.GenericError -> {
                sendScoreUploadError(result.error.code, result.error.message)
            }
        }
        _showScoresLoader.postValue(false)
    }

    private fun sendScoreUploadError(code: Int, message: String) {
        _scoreUploadError.postValue("Error code: $code - $message")
    }
}