package com.tfm.musiccommunityapp.ui.community.events.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.ui.utils.SingleLiveEvent
import com.tfm.musiccommunityapp.usecase.city.GetCitiesResult
import com.tfm.musiccommunityapp.usecase.city.GetCitiesUseCase
import com.tfm.musiccommunityapp.usecase.comment.DeleteCommentResult
import com.tfm.musiccommunityapp.usecase.comment.DeleteCommentUseCase
import com.tfm.musiccommunityapp.usecase.comment.GetPostCommentsResult
import com.tfm.musiccommunityapp.usecase.comment.GetPostCommentsUseCase
import com.tfm.musiccommunityapp.usecase.comment.PostOrRespondCommentResult
import com.tfm.musiccommunityapp.usecase.comment.PostOrRespondCommentUseCase
import com.tfm.musiccommunityapp.usecase.event.DeleteEventResult
import com.tfm.musiccommunityapp.usecase.event.DeleteEventUseCase
import com.tfm.musiccommunityapp.usecase.event.GetEventByIdResult
import com.tfm.musiccommunityapp.usecase.event.GetEventByIdUseCase
import com.tfm.musiccommunityapp.usecase.event.UpdateEventResult
import com.tfm.musiccommunityapp.usecase.event.UpdateEventUseCase
import com.tfm.musiccommunityapp.usecase.login.GetCurrentUserResult
import com.tfm.musiccommunityapp.usecase.login.GetCurrentUserUseCase
import com.tfm.musiccommunityapp.usecase.post.GetPostImageResult
import com.tfm.musiccommunityapp.usecase.post.GetPostImageUseCase
import com.tfm.musiccommunityapp.usecase.post.UploadPostImageResult
import com.tfm.musiccommunityapp.usecase.post.UploadPostImageUseCase
import com.tfm.musiccommunityapp.usecase.recommendations.CreateRecommendationResult
import com.tfm.musiccommunityapp.usecase.recommendations.CreateRecommendationUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.io.File

class EventDetailViewModel(
    private val getEventById: GetEventByIdUseCase,
    private val getPostImageByPostId: GetPostImageUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val updateEvent: UpdateEventUseCase,
    private val deleteEvent: DeleteEventUseCase,
    private val getCities: GetCitiesUseCase,
    private val createRecommendation: CreateRecommendationUseCase,
    private val getPostComments: GetPostCommentsUseCase,
    private val postOrRespondComment: PostOrRespondCommentUseCase,
    private val deleteComment: DeleteCommentUseCase,
    private val uploadImagePost: UploadPostImageUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    enum class EventOperationSuccess { UPDATE, DELETE, RECOMMEND, COMMENT, IMAGE_UPLOAD }

    private val _event: MutableLiveData<EventDomain?> = MutableLiveData()
    private val _cities: MutableLiveData<List<CityDomain>> = MutableLiveData()
    private val _comments: MutableLiveData<List<CommentDomain>> = MutableLiveData()
    private val _eventImageURL: SingleLiveEvent<String> = SingleLiveEvent()
    private val _getEventByIdError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showEventLoader: MutableLiveData<Boolean> = MutableLiveData()
    private val _isOwnerUser: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _isOperationSuccessful: SingleLiveEvent<EventOperationSuccess> =
        SingleLiveEvent()

    fun getEventLiveData() = _event as LiveData<EventDomain?>
    fun getCitiesLiveData() = _cities as LiveData<List<CityDomain>>
    fun getCommentsLiveData() = _comments as LiveData<List<CommentDomain>>
    fun getPostImageLiveData() = _eventImageURL as LiveData<String>
    fun getEventByIdError() = _getEventByIdError as LiveData<String>
    fun isEventLoading() = _showEventLoader as LiveData<Boolean>
    fun isUserOwnerLiveData() = _isOwnerUser as LiveData<Boolean>
    fun isOperationSuccessfulLiveData() =
        _isOperationSuccessful as LiveData<EventOperationSuccess>

    fun setUpEvent(eventId: Long) {
        viewModelScope.launch(dispatcher) {
            _showEventLoader.postValue(true)
            handleGetEventByIdResult(getEventById(eventId))
            handleGetPostImageResult(getPostImageByPostId(eventId))
            handleGetCurrentUserResult(getCurrentUser())
            handleGetCitiesResult(getCities(null))
            handleGetCommentsResult(getPostComments(eventId))
        }
    }

    fun reloadPostComments(eventId: Long) {
        viewModelScope.launch(dispatcher) {
            handleGetCommentsResult(getPostComments(eventId))
        }
    }

    private fun handleGetPostImageResult(result: GetPostImageResult) {
        when (result) {
            is GetPostImageResult.Success -> _eventImageURL.postValue(result.imageUrl)
        }
    }

    private fun handleGetEventByIdResult(result: GetEventByIdResult) {
        when (result) {
            GetEventByIdResult.NoEvent -> {
                _getEventByIdError.postValue("Error code: 404 - Event not found")
            }

            is GetEventByIdResult.Success -> {
                _event.postValue(result.event)
            }
        }
        _showEventLoader.postValue(false)
    }

    private fun handleGetCurrentUserResult(result: GetCurrentUserResult) {
        when (result) {
            is GetCurrentUserResult.Success -> {
                _isOwnerUser.postValue(result.user?.let { _event.value?.login == it.login }
                    ?: false)
            }

            GetCurrentUserResult.NoUser -> _isOwnerUser.postValue(false)
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

    fun sendDeleteEvent() {
        viewModelScope.launch(dispatcher) {
            _showEventLoader.postValue(true)
            handleDeleteEventResult(deleteEvent(_event.value?.id ?: 0))
        }
    }

    private fun handleDeleteEventResult(result: DeleteEventResult) {
        when (result) {
            is DeleteEventResult.Success ->
                _isOperationSuccessful.postValue(EventOperationSuccess.DELETE)

            is DeleteEventResult.GenericError ->
                _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is DeleteEventResult.NetworkError ->
                _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    fun sendUpdateEvent(event: EventDomain) {
        viewModelScope.launch(dispatcher) {
            _showEventLoader.postValue(true)
            handleUpdateEventResult(
                updateEvent(event),
                event.image
            )
        }
    }

    private suspend fun handleUpdateEventResult(
        result: UpdateEventResult,
        image: File?
    ) {
        when (result) {
            is UpdateEventResult.Success ->
                if (image != null) {
                    handleUploadImageToPost(
                        uploadImagePost(result.id, image),
                        EventOperationSuccess.UPDATE
                    )
                } else {
                    _isOperationSuccessful.postValue(EventOperationSuccess.UPDATE)
                }

            is UpdateEventResult.GenericError ->
                _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is UpdateEventResult.NetworkError ->
                _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    private fun handleUploadImageToPost(
        result: UploadPostImageResult,
        operation: EventOperationSuccess
    ) {
        when (result) {
            is UploadPostImageResult.Success -> {
                _isOperationSuccessful.postValue(EventOperationSuccess.IMAGE_UPLOAD)
                _isOperationSuccessful.postValue(operation)
            }

            is UploadPostImageResult.GenericError -> _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is UploadPostImageResult.NetworkError -> _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is UploadPostImageResult.NotFoundError -> _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is UploadPostImageResult.ValidationError -> _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    fun sendCreateRecommendation(recommendation: RecommendationDomain) {
        viewModelScope.launch(dispatcher) {
            _showEventLoader.postValue(true)
            handleCreateRecommendationResult(createRecommendation(recommendation))
        }
    }

    private fun handleCreateRecommendationResult(result: CreateRecommendationResult) {
        when (result) {
            is CreateRecommendationResult.Success -> {
                _isOperationSuccessful.postValue(EventOperationSuccess.RECOMMEND)
            }

            is CreateRecommendationResult.GenericError -> {
                _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }

            is CreateRecommendationResult.NetworkError -> {
                _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
        }
    }

    fun sendPostComment(comment: CommentDomain) {
        viewModelScope.launch(dispatcher) {
            handlePostCommentResult(postOrRespondComment(_event.value?.id ?: 0, null, comment))
        }
    }

    fun sendResponseComment(commentId: Long, comment: CommentDomain) {
        viewModelScope.launch(dispatcher) {
            handlePostCommentResult(
                postOrRespondComment(
                    _event.value?.id ?: 0,
                    commentId,
                    comment
                )
            )
        }
    }

    fun sendDeleteComment(comment: CommentDomain) {
        viewModelScope.launch(dispatcher) {
            handleDeleteComment(deleteComment(_event.value?.id ?: 0, comment.id))
        }
    }

    private fun handlePostCommentResult(result: PostOrRespondCommentResult) {
        when (result) {
            is PostOrRespondCommentResult.Success -> {
                _isOperationSuccessful.postValue(EventOperationSuccess.COMMENT)
            }

            is PostOrRespondCommentResult.GenericError -> {
                _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }

            is PostOrRespondCommentResult.NetworkError -> {
                _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
        }
    }

    private fun handleDeleteComment(result: DeleteCommentResult) {
        when (result) {
            is DeleteCommentResult.Success -> {
                _isOperationSuccessful.postValue(EventOperationSuccess.COMMENT)
            }

            is DeleteCommentResult.GenericError -> {
                _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }

            is DeleteCommentResult.NetworkError -> {
                _getEventByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
        }
    }

}