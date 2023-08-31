package com.tfm.musiccommunityapp.ui.community.advertisements.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.advertisement.DeleteAdvertisementResult
import com.tfm.musiccommunityapp.domain.interactor.advertisement.DeleteAdvertisementUseCase
import com.tfm.musiccommunityapp.domain.interactor.advertisement.GetAdvertisementByIdResult
import com.tfm.musiccommunityapp.domain.interactor.advertisement.GetAdvertisementByIdUseCase
import com.tfm.musiccommunityapp.domain.interactor.advertisement.UpdateAdvertisementResult
import com.tfm.musiccommunityapp.domain.interactor.advertisement.UpdateAdvertisementUseCase
import com.tfm.musiccommunityapp.domain.interactor.city.GetCitiesResult
import com.tfm.musiccommunityapp.domain.interactor.city.GetCitiesUseCase
import com.tfm.musiccommunityapp.domain.interactor.comment.DeleteCommentUseCase
import com.tfm.musiccommunityapp.domain.interactor.comment.GetPostCommentsResult
import com.tfm.musiccommunityapp.domain.interactor.comment.GetPostCommentsUseCase
import com.tfm.musiccommunityapp.domain.interactor.comment.PostOrRespondCommentUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserResult
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageResult
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageUseCase
import com.tfm.musiccommunityapp.domain.interactor.recommendations.CreateRecommendationResult
import com.tfm.musiccommunityapp.domain.interactor.recommendations.CreateRecommendationUseCase
import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class AdvertisementDetailViewModel(
    private val getAdvertisementById: GetAdvertisementByIdUseCase,
    private val getPostImageByPostId: GetPostImageUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val updateAdvertisement: UpdateAdvertisementUseCase,
    private val deleteAdvertisement: DeleteAdvertisementUseCase,
    private val getCities: GetCitiesUseCase,
    private val createRecommendation: CreateRecommendationUseCase,
    private val getPostComments: GetPostCommentsUseCase,
    private val postOrRespondComment: PostOrRespondCommentUseCase,
    private val deleteComment: DeleteCommentUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    enum class AdvertisementOperationSuccess { UPDATE, DELETE, RECOMMEND }

    private val _advertisement: MutableLiveData<AdvertisementDomain?> = MutableLiveData()
    private val _cities: MutableLiveData<List<CityDomain>> = MutableLiveData()
    private val _comments: MutableLiveData<List<CommentDomain>> = MutableLiveData()
    private val _advertisementImageURL: SingleLiveEvent<String> = SingleLiveEvent()
    private val _getAdvertisementByIdError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showAdvertisementLoader: MutableLiveData<Boolean> = MutableLiveData()
    private val _isOwnerUser: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _isOperationSuccessful: SingleLiveEvent<AdvertisementOperationSuccess> =
        SingleLiveEvent()

    fun getAdvertisementLiveData() = _advertisement as LiveData<AdvertisementDomain?>
    fun getCitiesLiveData() = _cities as LiveData<List<CityDomain>>
    fun getCommentsLiveData() = _comments as LiveData<List<CommentDomain>>
    fun getPostImageLiveData() = _advertisementImageURL as LiveData<String>
    fun getAdvertisementByIdError() = _getAdvertisementByIdError as LiveData<String>
    fun isAdvertisementLoading() = _showAdvertisementLoader as LiveData<Boolean>
    fun isUserOwnerLiveData() = _isOwnerUser as LiveData<Boolean>
    fun isOperationSuccessfulLiveData() =
        _isOperationSuccessful as LiveData<AdvertisementOperationSuccess>

    fun setUpAdvertisement(advertisementId: Long) {
        viewModelScope.launch(dispatcher) {
            _showAdvertisementLoader.postValue(true)
            handleGetAdvertisementByIdResult(getAdvertisementById(advertisementId))
            handleGetPostImageResult(getPostImageByPostId(advertisementId))
            handleGetCurrentUserResult(getCurrentUser())
            handleGetCitiesResult(getCities(null))
            handleGetCommentsResult(getPostComments(advertisementId))
        }
    }

    private fun handleGetPostImageResult(result: GetPostImageResult) {
        when (result) {
            is GetPostImageResult.Success -> _advertisementImageURL.postValue(result.imageUrl)
        }
    }

    private fun handleGetAdvertisementByIdResult(result: GetAdvertisementByIdResult) {
        when (result) {
            GetAdvertisementByIdResult.NoAdvertisement -> {
                _getAdvertisementByIdError.postValue("Error code: 404 - Advertisement not found")
            }

            is GetAdvertisementByIdResult.Success -> {
                _advertisement.postValue(result.advertisement)
            }
        }
        _showAdvertisementLoader.postValue(false)
    }

    private fun handleGetCurrentUserResult(result: GetCurrentUserResult) {
        when (result) {
            is GetCurrentUserResult.Success -> {
                _isOwnerUser.postValue(result.user?.let { _advertisement.value?.login == it.login }
                    ?: false)
            }

            GetCurrentUserResult.NoUser -> _isOwnerUser.postValue(false)
        }
    }

    private fun handleGetCitiesResult(result: GetCitiesResult) {
        when (result) {
            is GetCitiesResult.Success -> {
                _cities.postValue(result.cities)
            }

            else -> {
                _cities.postValue(emptyList())
            }
        }
    }

    private fun handleGetCommentsResult(result: GetPostCommentsResult) {
        when (result) {
            is GetPostCommentsResult.Success -> {
                _comments.postValue(result.comments)
            }

            else -> {
                _comments.postValue(emptyList())
            }
        }
    }

    fun sendDeleteAdvertisement() {
        viewModelScope.launch(dispatcher) {
            _showAdvertisementLoader.postValue(true)
            handleDeleteAdvertisementResult(deleteAdvertisement(_advertisement.value?.id ?: 0))
        }
    }

    private fun handleDeleteAdvertisementResult(result: DeleteAdvertisementResult) {
        when (result) {
            is DeleteAdvertisementResult.Success ->
                _isOperationSuccessful.postValue(AdvertisementOperationSuccess.DELETE)

            is DeleteAdvertisementResult.GenericError ->
                _getAdvertisementByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is DeleteAdvertisementResult.NetworkError ->
                _getAdvertisementByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    fun sendUpdateAdvertisement(ad: AdvertisementDomain) {
        viewModelScope.launch(dispatcher) {
            _showAdvertisementLoader.postValue(true)
            handleUpdateAdvertisementResult(updateAdvertisement(ad))
        }
    }

    private fun handleUpdateAdvertisementResult(result: UpdateAdvertisementResult) {
        when (result) {
            is UpdateAdvertisementResult.Success ->
                _isOperationSuccessful.postValue(AdvertisementOperationSuccess.DELETE)

            is UpdateAdvertisementResult.GenericError ->
                _getAdvertisementByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is UpdateAdvertisementResult.NetworkError ->
                _getAdvertisementByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    fun sendCreateRecommendation(recommendation: RecommendationDomain) {
        viewModelScope.launch(dispatcher) {
            _showAdvertisementLoader.postValue(true)
            handleCreateRecommendationResult(createRecommendation(recommendation))
        }
    }

    private fun handleCreateRecommendationResult(result: CreateRecommendationResult) {
        when(result) {
            is CreateRecommendationResult.Success -> {
                _isOperationSuccessful.postValue(AdvertisementOperationSuccess.RECOMMEND)
            }
            is CreateRecommendationResult.GenericError -> {
                _getAdvertisementByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
            is CreateRecommendationResult.NetworkError -> {
                _getAdvertisementByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
        }
    }

}