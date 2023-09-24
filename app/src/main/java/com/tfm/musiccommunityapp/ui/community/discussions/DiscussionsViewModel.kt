package com.tfm.musiccommunityapp.ui.community.discussions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.ui.utils.SingleLiveEvent
import com.tfm.musiccommunityapp.usecase.discussion.GetDiscussionsResult
import com.tfm.musiccommunityapp.usecase.discussion.GetDiscussionsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class DiscussionsViewModel(
    private val getDiscussions: GetDiscussionsUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _discussions:  MutableLiveData<List<DiscussionDomain>> = MutableLiveData()
    private val _communityDiscussionsError: SingleLiveEvent<String> = SingleLiveEvent()
    private val showDiscussionsLoader: MutableLiveData<Boolean> = MutableLiveData()

    fun getDiscussionsLiveData() = _discussions as LiveData<List<DiscussionDomain>>
    fun getCommunityDiscussionsError() = _communityDiscussionsError as LiveData<String>
    fun isDiscussionsLoading() = showDiscussionsLoader as LiveData<Boolean>

    fun setUpDiscussions(searchTerm: String?) {
        viewModelScope.launch(dispatcher) {
            showDiscussionsLoader.postValue(true)
            handleGetDiscussionsResult(getDiscussions(searchTerm))
        }
    }

    private fun handleGetDiscussionsResult(result: GetDiscussionsResult) {
        when (result) {
            is GetDiscussionsResult.Success -> {
                _discussions.postValue(result.discussions)
            }
            is GetDiscussionsResult.NetworkError -> {
                _discussions.postValue(emptyList())
                sendCommunityDiscussionsError(result.error.code, result.error.message)
            }
            is GetDiscussionsResult.GenericError -> {
                _discussions.postValue(emptyList())
                sendCommunityDiscussionsError(result.error.code, result.error.message)
            }
        }
        showDiscussionsLoader.postValue(false)
    }

    private fun sendCommunityDiscussionsError(code: Int, message: String) {
        _communityDiscussionsError.postValue("Error code: $code - $message")
    }
}