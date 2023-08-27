package com.tfm.musiccommunityapp.ui.community.discussions.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.discussion.GetDiscussionByIdResult
import com.tfm.musiccommunityapp.domain.interactor.discussion.GetDiscussionByIdUseCase
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageResult
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageUseCase
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class DiscussionDetailViewModel(
    private val getDiscussionById: GetDiscussionByIdUseCase,
    private val getPostImageByPostId: GetPostImageUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _discussion: MutableLiveData<DiscussionDomain?> = MutableLiveData()
    private val _discussionImageURL: SingleLiveEvent<String> = SingleLiveEvent()
    private val _getDiscussionByIdError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showDiscussionLoader: MutableLiveData<Boolean> = MutableLiveData()

    fun getDiscussionLiveData() = _discussion as LiveData<DiscussionDomain?>
    fun getPostImageLiveData() = _discussionImageURL as LiveData<String>
    fun getDiscussionByIdError() = _getDiscussionByIdError as LiveData<String>
    fun isDiscussionLoading() = _showDiscussionLoader as LiveData<Boolean>

    fun setUpDiscussion(discussionId: Long) {
        viewModelScope.launch(dispatcher) {
            _showDiscussionLoader.postValue(true)
            handleGetDiscussionByIdResult(getDiscussionById(discussionId))
            handleGetPostImageResult(getPostImageByPostId(discussionId))
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
}