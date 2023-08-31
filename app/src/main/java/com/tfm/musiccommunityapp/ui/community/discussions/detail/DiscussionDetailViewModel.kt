package com.tfm.musiccommunityapp.ui.community.discussions.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.discussion.DeleteDiscussionResult
import com.tfm.musiccommunityapp.domain.interactor.discussion.DeleteDiscussionUseCase
import com.tfm.musiccommunityapp.domain.interactor.discussion.GetDiscussionByIdResult
import com.tfm.musiccommunityapp.domain.interactor.discussion.GetDiscussionByIdUseCase
import com.tfm.musiccommunityapp.domain.interactor.discussion.UpdateDiscussionResult
import com.tfm.musiccommunityapp.domain.interactor.discussion.UpdateDiscussionUseCase
import com.tfm.musiccommunityapp.domain.interactor.event.UpdateEventResult
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserResult
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageResult
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageUseCase
import com.tfm.musiccommunityapp.domain.interactor.recommendations.CreateRecommendationResult
import com.tfm.musiccommunityapp.domain.interactor.recommendations.CreateRecommendationUseCase
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.ui.community.events.detail.EventDetailViewModel
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class DiscussionDetailViewModel(
    private val getDiscussionById: GetDiscussionByIdUseCase,
    private val getPostImageByPostId: GetPostImageUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val updateDiscussion: UpdateDiscussionUseCase,
    private val deleteDiscussion: DeleteDiscussionUseCase,
    private val createRecommendation: CreateRecommendationUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    enum class DiscussionOperationSuccess { UPDATE, DELETE, RECOMMEND }

    private val _discussion: MutableLiveData<DiscussionDomain?> = MutableLiveData()
    private val _discussionImageURL: SingleLiveEvent<String> = SingleLiveEvent()
    private val _getDiscussionByIdError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showDiscussionLoader: MutableLiveData<Boolean> = MutableLiveData()
    private val _isOwnerUser: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _isOperationSuccessful: SingleLiveEvent<DiscussionOperationSuccess> =
        SingleLiveEvent()

    fun getDiscussionLiveData() = _discussion as LiveData<DiscussionDomain?>
    fun getPostImageLiveData() = _discussionImageURL as LiveData<String>
    fun getDiscussionByIdError() = _getDiscussionByIdError as LiveData<String>
    fun isDiscussionLoading() = _showDiscussionLoader as LiveData<Boolean>
    fun isUserOwnerLiveData() = _isOwnerUser as LiveData<Boolean>
    fun isOperationSuccessfulLiveData() =
        _isOperationSuccessful as LiveData<DiscussionOperationSuccess>

    fun setUpDiscussion(discussionId: Long) {
        viewModelScope.launch(dispatcher) {
            _showDiscussionLoader.postValue(true)
            handleGetDiscussionByIdResult(getDiscussionById(discussionId))
            handleGetPostImageResult(getPostImageByPostId(discussionId))
            handleGetCurrentUserResult(getCurrentUser())
        }
    }

    private fun handleGetPostImageResult(result: GetPostImageResult) {
        when (result) {
            is GetPostImageResult.Success -> _discussionImageURL.postValue(result.imageUrl)
        }
    }

    private fun handleGetDiscussionByIdResult(result: GetDiscussionByIdResult) {
        when (result) {
            GetDiscussionByIdResult.NoDiscussion -> {
                _getDiscussionByIdError.postValue("Error code: 404 - Discussion not found")
            }

            is GetDiscussionByIdResult.Success -> {
                _discussion.postValue(result.discussion)
            }
        }
        _showDiscussionLoader.postValue(false)
    }

    private fun handleGetCurrentUserResult(result: GetCurrentUserResult) {
        when (result) {
            is GetCurrentUserResult.Success -> {
                _isOwnerUser.postValue(result.user?.let { _discussion.value?.login == it.login }
                    ?: false)
            }

            GetCurrentUserResult.NoUser -> _isOwnerUser.postValue(false)
        }
    }

    fun sendDeleteDiscussion() {
        viewModelScope.launch(dispatcher) {
            _showDiscussionLoader.postValue(true)
            handleDeleteDiscussionResult(deleteDiscussion(_discussion.value?.id ?: 0))
        }
    }

    private fun handleDeleteDiscussionResult(result: DeleteDiscussionResult) {
        when (result) {
            is DeleteDiscussionResult.Success ->
                _isOperationSuccessful.postValue(DiscussionOperationSuccess.DELETE)

            is DeleteDiscussionResult.GenericError ->
                _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is DeleteDiscussionResult.NetworkError ->
                _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    fun sendUpdateDiscussion(discussion: DiscussionDomain) {
        viewModelScope.launch(dispatcher) {
            _showDiscussionLoader.postValue(true)
            handleUpdateDiscussionResult(updateDiscussion(discussion))
        }
    }

    private fun handleUpdateDiscussionResult(result: UpdateDiscussionResult) {
        when (result) {
            is UpdateDiscussionResult.Success ->
                _isOperationSuccessful.postValue(DiscussionOperationSuccess.UPDATE)

            is UpdateDiscussionResult.GenericError ->
                _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is UpdateDiscussionResult.NetworkError ->
                _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    fun sendCreateRecommendation(recommendation: RecommendationDomain) {
        viewModelScope.launch(dispatcher) {
            _showDiscussionLoader.postValue(true)
            handleCreateRecommendationResult(createRecommendation(recommendation))
        }
    }

    private fun handleCreateRecommendationResult(result: CreateRecommendationResult) {
        when(result) {
            is CreateRecommendationResult.Success -> {
                _isOperationSuccessful.postValue(DiscussionOperationSuccess.RECOMMEND)
            }
            is CreateRecommendationResult.GenericError -> {
                _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
            is CreateRecommendationResult.NetworkError -> {
                _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
        }
    }

}