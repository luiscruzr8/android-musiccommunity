package com.tfm.musiccommunityapp.ui.community.discussions.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.ui.utils.SingleLiveEvent
import com.tfm.musiccommunityapp.usecase.comment.DeleteCommentResult
import com.tfm.musiccommunityapp.usecase.comment.DeleteCommentUseCase
import com.tfm.musiccommunityapp.usecase.comment.GetPostCommentsResult
import com.tfm.musiccommunityapp.usecase.comment.GetPostCommentsUseCase
import com.tfm.musiccommunityapp.usecase.comment.PostOrRespondCommentResult
import com.tfm.musiccommunityapp.usecase.comment.PostOrRespondCommentUseCase
import com.tfm.musiccommunityapp.usecase.discussion.DeleteDiscussionResult
import com.tfm.musiccommunityapp.usecase.discussion.DeleteDiscussionUseCase
import com.tfm.musiccommunityapp.usecase.discussion.GetDiscussionByIdResult
import com.tfm.musiccommunityapp.usecase.discussion.GetDiscussionByIdUseCase
import com.tfm.musiccommunityapp.usecase.discussion.UpdateDiscussionResult
import com.tfm.musiccommunityapp.usecase.discussion.UpdateDiscussionUseCase
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

class DiscussionDetailViewModel(
    private val getDiscussionById: GetDiscussionByIdUseCase,
    private val getPostImageByPostId: GetPostImageUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val updateDiscussion: UpdateDiscussionUseCase,
    private val deleteDiscussion: DeleteDiscussionUseCase,
    private val createRecommendation: CreateRecommendationUseCase,
    private val getPostComments: GetPostCommentsUseCase,
    private val postOrRespondComment: PostOrRespondCommentUseCase,
    private val deleteComment: DeleteCommentUseCase,
    private val uploadImagePost: UploadPostImageUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    enum class DiscussionOperationSuccess { UPDATE, DELETE, RECOMMEND, COMMENT, IMAGE_UPLOAD }

    private val _discussion: MutableLiveData<DiscussionDomain?> = MutableLiveData()
    private val _comments: MutableLiveData<List<CommentDomain>> = MutableLiveData()
    private val _discussionImageURL: SingleLiveEvent<String> = SingleLiveEvent()
    private val _getDiscussionByIdError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showDiscussionLoader: MutableLiveData<Boolean> = MutableLiveData()
    private val _isOwnerUser: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _isOperationSuccessful: SingleLiveEvent<DiscussionOperationSuccess> =
        SingleLiveEvent()

    fun getDiscussionLiveData() = _discussion as LiveData<DiscussionDomain?>
    fun getCommentsLiveData() = _comments as LiveData<List<CommentDomain>>
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
            handleGetCommentsResult(getPostComments(discussionId))
        }
    }

    fun reloadPostComments(discussionId: Long) {
        viewModelScope.launch(dispatcher) {
            handleGetCommentsResult(getPostComments(discussionId))
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
            handleUpdateDiscussionResult(
                updateDiscussion(discussion),
                discussion.image
            )
        }
    }

    private suspend fun handleUpdateDiscussionResult(
        result: UpdateDiscussionResult,
        image: File?
    ) {
        when (result) {
            is UpdateDiscussionResult.Success ->
                if (image != null) {
                    handleUploadImageToPost(
                        uploadImagePost(result.id, image),
                        DiscussionOperationSuccess.UPDATE
                    )
                } else {
                    _isOperationSuccessful.postValue(DiscussionOperationSuccess.UPDATE)
                }

            is UpdateDiscussionResult.GenericError ->
                _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is UpdateDiscussionResult.NetworkError ->
                _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    private fun handleUploadImageToPost(
        result: UploadPostImageResult,
        operation: DiscussionOperationSuccess
    ) {
        when (result) {
            is UploadPostImageResult.Success -> {
                _isOperationSuccessful.postValue(DiscussionOperationSuccess.IMAGE_UPLOAD)
                _isOperationSuccessful.postValue(operation)
            }

            is UploadPostImageResult.GenericError -> _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is UploadPostImageResult.NetworkError -> _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is UploadPostImageResult.NotFoundError -> _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            is UploadPostImageResult.ValidationError -> _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    fun sendCreateRecommendation(recommendation: RecommendationDomain) {
        viewModelScope.launch(dispatcher) {
            _showDiscussionLoader.postValue(true)
            handleCreateRecommendationResult(createRecommendation(recommendation))
        }
    }

    private fun handleCreateRecommendationResult(result: CreateRecommendationResult) {
        when (result) {
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

    fun sendPostComment(comment: CommentDomain) {
        viewModelScope.launch(dispatcher) {
            handlePostCommentResult(postOrRespondComment(_discussion.value?.id ?: 0, null, comment))
        }
    }

    fun sendResponseComment(commentId: Long, comment: CommentDomain) {
        viewModelScope.launch(dispatcher) {
            handlePostCommentResult(
                postOrRespondComment(
                    _discussion.value?.id ?: 0,
                    commentId,
                    comment
                )
            )
        }
    }

    fun sendDeleteComment(comment: CommentDomain) {
        viewModelScope.launch(dispatcher) {
            handleDeleteComment(deleteComment(_discussion.value?.id ?: 0, comment.id))
        }
    }

    private fun handlePostCommentResult(result: PostOrRespondCommentResult) {
        when (result) {
            is PostOrRespondCommentResult.Success -> {
                _isOperationSuccessful.postValue(DiscussionOperationSuccess.COMMENT)
            }

            is PostOrRespondCommentResult.GenericError -> {
                _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }

            is PostOrRespondCommentResult.NetworkError -> {
                _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
        }
    }

    private fun handleDeleteComment(result: DeleteCommentResult) {
        when (result) {
            is DeleteCommentResult.Success -> {
                _isOperationSuccessful.postValue(DiscussionOperationSuccess.COMMENT)
            }

            is DeleteCommentResult.GenericError -> {
                _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }

            is DeleteCommentResult.NetworkError -> {
                _getDiscussionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
        }
    }

}