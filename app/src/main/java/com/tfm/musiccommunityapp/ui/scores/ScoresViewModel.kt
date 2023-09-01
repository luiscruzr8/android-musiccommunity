package com.tfm.musiccommunityapp.ui.scores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserResult
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.score.GetUserScoresResult
import com.tfm.musiccommunityapp.domain.interactor.score.GetUserScoresUseCase
import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class ScoresViewModel(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getPrivateUserScores: GetUserScoresUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _scores: MutableLiveData<List<ScoreDomain>> = MutableLiveData()
    private val _scoresError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showScoresLoader = MutableLiveData<Boolean>()

    fun getScoresLiveData() = _scores as LiveData<List<ScoreDomain>>
    fun getScoresError() = _scoresError as LiveData<String>
    fun isScoresLoading() = _showScoresLoader as LiveData<Boolean>

    fun setUpScores(searchTerm: String?) {
        viewModelScope.launch(dispatcher) {
            _showScoresLoader.postValue(true)
            handleCurrentUser(searchTerm, getCurrentUser())
        }
    }

    private suspend fun handleCurrentUser(searchTerm: String?, result: GetCurrentUserResult) {
        when (result) {
            is GetCurrentUserResult.Success -> {
                handleGetScoresResult(getPrivateUserScores(result.user?.login, searchTerm, false))
            }
            is GetCurrentUserResult.NoUser -> {
                sendScoresError(404, "User not found")
            }
        }
    }

    private fun handleGetScoresResult(result: GetUserScoresResult) {
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
}