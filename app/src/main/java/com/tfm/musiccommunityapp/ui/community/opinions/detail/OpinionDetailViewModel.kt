package com.tfm.musiccommunityapp.ui.community.opinions.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.comment.DeleteCommentResult
import com.tfm.musiccommunityapp.domain.interactor.comment.DeleteCommentUseCase
import com.tfm.musiccommunityapp.domain.interactor.comment.GetPostCommentsResult
import com.tfm.musiccommunityapp.domain.interactor.comment.GetPostCommentsUseCase
import com.tfm.musiccommunityapp.domain.interactor.comment.PostOrRespondCommentResult
import com.tfm.musiccommunityapp.domain.interactor.comment.PostOrRespondCommentUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserResult
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.opinion.DeleteOpinionResult
import com.tfm.musiccommunityapp.domain.interactor.opinion.DeleteOpinionUseCase
import com.tfm.musiccommunityapp.domain.interactor.opinion.GetOpinionByIdResult
import com.tfm.musiccommunityapp.domain.interactor.opinion.GetOpinionByIdUseCase
import com.tfm.musiccommunityapp.domain.interactor.opinion.UpdateOpinionUseCase
import com.tfm.musiccommunityapp.domain.interactor.recommendations.CreateRecommendationResult
import com.tfm.musiccommunityapp.domain.interactor.recommendations.CreateRecommendationUseCase
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.domain.model.OpinionDomain
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class OpinionDetailViewModel(
    private val getOpinionById: GetOpinionByIdUseCase,
    //private val getScoreById: GetScoreByIdUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val updateOpinion: UpdateOpinionUseCase,
    private val deleteOpinion: DeleteOpinionUseCase,
    private val createRecommendation: CreateRecommendationUseCase,
    private val getPostComments: GetPostCommentsUseCase,
    private val postOrRespondComment: PostOrRespondCommentUseCase,
    private val deleteComment: DeleteCommentUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    enum class OpinionOperationSuccess { UPDATE, DELETE, RECOMMEND, COMMENT }

    private val _opinion: MutableLiveData<OpinionDomain?> = MutableLiveData()
    private val _comments: MutableLiveData<List<CommentDomain>> = MutableLiveData()
    private val _getOpinionByIdError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showOpinionLoader: MutableLiveData<Boolean> = MutableLiveData()
    private val _isOwnerUser: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _isOperationSuccessful: SingleLiveEvent<OpinionOperationSuccess> = SingleLiveEvent()

    fun getOpinionLiveData() = _opinion as LiveData<OpinionDomain?>
    fun getCommentsLiveData() = _comments as LiveData<List<CommentDomain>>
    fun getOpinionByIdError() = _getOpinionByIdError as LiveData<String>
    fun isOpinionLoading() = _showOpinionLoader as LiveData<Boolean>
    fun isUserOwnerLiveData() = _isOwnerUser as LiveData<Boolean>
    fun isOperationSuccessfulLiveData() =
        _isOperationSuccessful as LiveData<OpinionOperationSuccess>

    fun setUpOpinion(opinionId: Long) {
        viewModelScope.launch(dispatcher) {
            _showOpinionLoader.postValue(true)
            handleGetOpinionByIdResult(getOpinionById(opinionId))
            handleGetCurrentUserResult(getCurrentUser())

            handleGetCommentsResult(getPostComments(opinionId))

            //TODO: replace when scores are implemented
            handleGetScoreResult("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
        }
    }

    fun reloadPostComments(opinionId: Long) {
        viewModelScope.launch(dispatcher) {
            handleGetCommentsResult(getPostComments(opinionId))
        }
    }

    private fun handleGetOpinionByIdResult(result: GetOpinionByIdResult) {
        when (result) {
            GetOpinionByIdResult.NoOpinion -> {
                _getOpinionByIdError.postValue("Error code: 404 - Opinion not found")
            }

            is GetOpinionByIdResult.Success -> {
                _opinion.postValue(result.opinion)
            }
        }
        _showOpinionLoader.postValue(false)
    }

    private fun handleGetCurrentUserResult(result: GetCurrentUserResult) {
        when (result) {
            is GetCurrentUserResult.Success -> {
                _isOwnerUser.postValue(result.user?.let { _opinion.value?.login == it.login }
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

    //TODO: replace when scores are implemented
    private val _score: SingleLiveEvent<String> = SingleLiveEvent()
    fun getScoreLiveData() = _score as LiveData<String>
    private fun handleGetScoreResult(result: String) {
        _score.postValue(result)
    }

    fun sendDeleteOpinion() {
        viewModelScope.launch(dispatcher) {
            _showOpinionLoader.postValue(true)
            handleDeleteOpinionResult(deleteOpinion(_opinion.value?.id ?: 0))
        }
    }

    private fun handleDeleteOpinionResult(result: DeleteOpinionResult) {
        when (result) {
            is DeleteOpinionResult.Success ->
                _isOperationSuccessful.postValue(OpinionOperationSuccess.DELETE)

            is DeleteOpinionResult.GenericError ->
                _getOpinionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is DeleteOpinionResult.NetworkError ->
                _getOpinionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    fun sendCreateRecommendation(recommendation: RecommendationDomain) {
        viewModelScope.launch(dispatcher) {
            _showOpinionLoader.postValue(true)
            handleCreateRecommendationResult(createRecommendation(recommendation))
        }
    }

    private fun handleCreateRecommendationResult(result: CreateRecommendationResult) {
        when(result) {
            is CreateRecommendationResult.Success -> {
                _isOperationSuccessful.postValue(OpinionOperationSuccess.RECOMMEND)
            }

            is CreateRecommendationResult.GenericError -> {
                _getOpinionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }

            is CreateRecommendationResult.NetworkError -> {
                _getOpinionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
        }
    }

    fun sendPostComment(comment: CommentDomain) {
        viewModelScope.launch(dispatcher) {
            handlePostCommentResult(postOrRespondComment(_opinion.value?.id ?: 0, null, comment))
        }
    }

    fun sendResponseComment(commentId: Long, comment: CommentDomain) {
        viewModelScope.launch(dispatcher) {
            handlePostCommentResult(
                postOrRespondComment(
                    _opinion.value?.id ?: 0,
                    commentId,
                    comment
                )
            )
        }
    }

    fun sendDeleteComment(comment: CommentDomain) {
        viewModelScope.launch(dispatcher) {
            handleDeleteComment(deleteComment(_opinion.value?.id ?: 0, comment.id))
        }
    }

    private fun handlePostCommentResult(result: PostOrRespondCommentResult) {
        when (result) {
            is PostOrRespondCommentResult.Success -> {
                _isOperationSuccessful.postValue(OpinionOperationSuccess.COMMENT)
            }

            is PostOrRespondCommentResult.GenericError -> {
                _getOpinionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }

            is PostOrRespondCommentResult.NetworkError -> {
                _getOpinionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
        }
    }

    private fun handleDeleteComment(result: DeleteCommentResult) {
        when (result) {
            is DeleteCommentResult.Success -> {
                _isOperationSuccessful.postValue(OpinionOperationSuccess.COMMENT)
            }

            is DeleteCommentResult.GenericError -> {
                _getOpinionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }

            is DeleteCommentResult.NetworkError -> {
                _getOpinionByIdError.postValue("Error code: ${result.error.code} - ${result.error.message}")
            }
        }
    }
}