package com.tfm.musiccommunityapp.ui.community.recommendations.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserResult
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageResult
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageUseCase
import com.tfm.musiccommunityapp.domain.interactor.recommendations.DeleteRecommendationResult
import com.tfm.musiccommunityapp.domain.interactor.recommendations.DeleteRecommendationUseCase
import com.tfm.musiccommunityapp.domain.interactor.recommendations.GetRecommendationByIdResult
import com.tfm.musiccommunityapp.domain.interactor.recommendations.GetRecommendationByIdUseCase
import com.tfm.musiccommunityapp.domain.interactor.recommendations.RateRecommendationResult
import com.tfm.musiccommunityapp.domain.interactor.recommendations.RateRecommendationUseCase
import com.tfm.musiccommunityapp.domain.interactor.recommendations.UpdateRecommendationResult
import com.tfm.musiccommunityapp.domain.interactor.recommendations.UpdateRecommendationUseCase
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class RecommendationDetailViewModel(
    private val getRecommendationById: GetRecommendationByIdUseCase,
    private val getPostImageByPostId: GetPostImageUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val updateRecommendation: UpdateRecommendationUseCase,
    private val deleteRecommendation: DeleteRecommendationUseCase,
    private val rateRecommendation: RateRecommendationUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    enum class RecommendationOperationSuccess { UPDATE, DELETE, RATE }

    private val _recommendation: MutableLiveData<RecommendationDomain?> = MutableLiveData()
    private val _postImageURL: SingleLiveEvent<String> = SingleLiveEvent()
    private val _getRecommendationByIdError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showRecommendationLoader: MutableLiveData<Boolean> = MutableLiveData()
    private val _isOwnerUser: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _isOperationSuccessful: SingleLiveEvent<RecommendationOperationSuccess> =
        SingleLiveEvent()

    fun getRecommendationLiveData() = _recommendation as LiveData<RecommendationDomain?>
    fun getPostImageLiveData() = _postImageURL as LiveData<String>
    fun getRecommendationByIdError() = _getRecommendationByIdError as LiveData<String>
    fun isRecommendationLoading() = _showRecommendationLoader as LiveData<Boolean>
    fun isUserOwnerLiveData() = _isOwnerUser as LiveData<Boolean>
    fun getOperationSuccessfulLiveData() =
        _isOperationSuccessful as LiveData<RecommendationOperationSuccess>

    fun setUpRecommendation(recommendationId: Long) {
        viewModelScope.launch(dispatcher) {
            _showRecommendationLoader.postValue(true)
            handleGetRecommendationByIdResult(getRecommendationById(recommendationId))
            handleGetPostImageResult(getPostImageByPostId(recommendationId))
            handleGetCurrentUserResult(getCurrentUser())
        }
    }

    private fun handleGetPostImageResult(result: GetPostImageResult) {
        when (result) {
            is GetPostImageResult.Success -> _postImageURL.postValue(result.imageUrl)
        }
    }

    private fun handleGetRecommendationByIdResult(result: GetRecommendationByIdResult) {
        when (result) {
            is GetRecommendationByIdResult.Success -> {
                _recommendation.postValue(result.recommendation)
            }

            is GetRecommendationByIdResult.NetworkError -> {
                _getRecommendationByIdError.postValue("Error code: 404 - Discussion not found")
            }

            is GetRecommendationByIdResult.GenericError -> {
                _getRecommendationByIdError.postValue("Error code: 404 - Discussion not found")
            }
        }
        _showRecommendationLoader.postValue(false)
    }

    private fun handleGetCurrentUserResult(result: GetCurrentUserResult) {
        when (result) {
            is GetCurrentUserResult.Success -> {
                _isOwnerUser.postValue(result.user?.let { _recommendation.value?.login == it.login }
                    ?: false)
            }

            GetCurrentUserResult.NoUser -> _isOwnerUser.postValue(false)
        }
    }

    fun sendDeleteRecommendation() {
        viewModelScope.launch(dispatcher) {
            _showRecommendationLoader.postValue(true)
            handleDeleteRecommendationResult(deleteRecommendation(_recommendation.value?.id ?: 0))
        }
    }

    private fun handleDeleteRecommendationResult(result: DeleteRecommendationResult) {
        when (result) {
            is DeleteRecommendationResult.Success -> {
                _isOperationSuccessful.postValue(RecommendationOperationSuccess.DELETE)
            }

            is DeleteRecommendationResult.NetworkError -> {
                _getRecommendationByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }

            is DeleteRecommendationResult.GenericError -> {
                _getRecommendationByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
        }
    }

    fun sendUpdateRecommendation(recommendation: RecommendationDomain) {
        viewModelScope.launch(dispatcher) {
            _showRecommendationLoader.postValue(true)
            handleUpdateRecommendationResult(
                updateRecommendation(
                    recommendation.id,
                    recommendation
                )
            )
        }
    }

    private fun handleUpdateRecommendationResult(result: UpdateRecommendationResult) {
        when (result) {
            is UpdateRecommendationResult.Success -> {
                _isOperationSuccessful.postValue(RecommendationOperationSuccess.UPDATE)
            }

            is UpdateRecommendationResult.NetworkError -> {
                _getRecommendationByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }

            is UpdateRecommendationResult.GenericError -> {
                _getRecommendationByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
        }
    }

    fun sendRateRecommendation(id: Long, rating: Int) {
        viewModelScope.launch(dispatcher) {
            _showRecommendationLoader.postValue(true)
            handleRateRecommendationResult(rateRecommendation(id, rating))
        }
    }

    private fun handleRateRecommendationResult(result: RateRecommendationResult) {
        when (result) {
            is RateRecommendationResult.Success -> {
                _isOperationSuccessful.postValue(RecommendationOperationSuccess.RATE)
            }

            is RateRecommendationResult.NetworkError -> {
                _getRecommendationByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }

            is RateRecommendationResult.GenericError -> {
                _getRecommendationByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
        }
    }

}