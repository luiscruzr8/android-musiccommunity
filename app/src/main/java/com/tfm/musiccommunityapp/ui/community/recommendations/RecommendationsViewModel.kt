package com.tfm.musiccommunityapp.ui.community.recommendations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.ui.utils.SingleLiveEvent
import com.tfm.musiccommunityapp.usecase.recommendations.GetRecommendationsResult
import com.tfm.musiccommunityapp.usecase.recommendations.GetRecommendationsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class RecommendationsViewModel(
    private val getRecommendations: GetRecommendationsUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _recommendations: MutableLiveData<List<RecommendationDomain>> = MutableLiveData()
    private val _communityRecommendationsError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showRecommendationsLoader: MutableLiveData<Boolean> = MutableLiveData()

    fun getRecommendationsLiveData() = _recommendations as LiveData<List<RecommendationDomain>>
    fun getCommunityRecommendationsError() = _communityRecommendationsError as LiveData<String>
    fun isRecommendationsLoading() = _showRecommendationsLoader as LiveData<Boolean>

    fun setUpRecommendations(isTop10Selected: Boolean, searchTerm: String?) {
        viewModelScope.launch(dispatcher) {
            _showRecommendationsLoader.postValue(true)
            handleGetRecommendationsResult(getRecommendations(isTop10Selected, searchTerm))
        }
    }

    private fun handleGetRecommendationsResult(result: GetRecommendationsResult) {
        when (result) {
            is GetRecommendationsResult.Success -> {
                _recommendations.postValue(result.recommendations)
            }
            is GetRecommendationsResult.NetworkError -> {
                _recommendations.postValue(emptyList())
                sendCommunityRecommendationsError(result.error.code, result.error.message)
            }
            is GetRecommendationsResult.GenericError -> {
                _recommendations.postValue(emptyList())
                sendCommunityRecommendationsError(result.error.code, result.error.message)
            }
        }
        _showRecommendationsLoader.postValue(false)
    }

    private fun sendCommunityRecommendationsError(code: Int, message: String) {
        _communityRecommendationsError.postValue("Error code: $code - $message")
    }

}