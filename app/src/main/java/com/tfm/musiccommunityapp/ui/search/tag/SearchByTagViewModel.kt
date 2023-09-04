package com.tfm.musiccommunityapp.ui.search.tag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostByCityResult
import com.tfm.musiccommunityapp.domain.interactor.search.GetPostsByTagUseCase
import com.tfm.musiccommunityapp.domain.interactor.search.GetPostsByTagUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.search.GetUsersByTagUseCase
import com.tfm.musiccommunityapp.domain.interactor.search.GetUsersByTagUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.tag.GetTagsUseCase
import com.tfm.musiccommunityapp.domain.interactor.tag.GetTagsUseCaseResult
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.domain.model.TagDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class SearchByTagViewModel(
    private val getTags: GetTagsUseCase,
    private val searchPostsByTag: GetPostsByTagUseCase,
    private val searchUsersByInterests: GetUsersByTagUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _posts: MutableLiveData<List<GenericPostDomain>> = MutableLiveData()
    private val _users: MutableLiveData<List<ShortUserDomain>> = MutableLiveData()
    private val _tagsOrInterests: MutableLiveData<List<TagDomain>> = MutableLiveData()
    private val _postsErrors: SingleLiveEvent<String> = SingleLiveEvent()
    private val _usersErrors: SingleLiveEvent<String> = SingleLiveEvent()
    private val _tagsOrInterestsErrors: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showLoader: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun getPostsResult() = _posts as LiveData<List<GenericPostDomain>>
    fun getUsersResult() = _users as LiveData<List<ShortUserDomain>>
    fun getTagsOrInterestsResult() = _tagsOrInterests as LiveData<List<TagDomain>>
    fun getPostsErrors() = _postsErrors as LiveData<String>
    fun getUsersErrors() = _usersErrors as LiveData<String>
    fun getTagsOrInterestsErrors() = _tagsOrInterestsErrors as LiveData<String>
    fun isLoading() = _showLoader as LiveData<Boolean>

    fun setUpSearchByTag(tagName: String) {
        viewModelScope.launch(dispatcher) {
            _showLoader.postValue(true)
            handleGetTagsResult(getTags())
            if (tagName.isEmpty()) {
                _posts.postValue(emptyList())
                _showLoader.postValue(false)
            } else {
                handleGetPostsResult(searchPostsByTag(tagName))
            }
        }
    }

    fun setUpSearchByInterests(interest: String) {
        viewModelScope.launch(dispatcher) {
            _showLoader.postValue(true)
            handleGetTagsResult(getTags())
            if (interest.isEmpty()) {
                _users.postValue(emptyList())
                _showLoader.postValue(false)
            } else {
                handleGetUsersResult(searchUsersByInterests(interest))
            }
        }
    }

    private fun handleGetTagsResult(result: GetTagsUseCaseResult) {
        when (result) {
            is GetTagsUseCaseResult.Success ->
                _tagsOrInterests.postValue(result.tags)
            is GetTagsUseCaseResult.NetworkError ->
                sendTagsOrInterestsError(result.error.code, result.error.message)
            is GetTagsUseCaseResult.GenericError ->
                sendTagsOrInterestsError(result.error.code, result.error.message)
        }
    }

    private fun handleGetPostsResult(result: GetPostsByTagUseCaseResult) {
        when (result) {
            is GetPostsByTagUseCaseResult.Success ->
                _posts.postValue(result.posts)

            is GetPostsByTagUseCaseResult.GenericError ->
                sendPostsError(result.error.code, result.error.message)

            is GetPostsByTagUseCaseResult.NetworkError ->
                sendPostsError(result.error.code, result.error.message)
        }
        _showLoader.postValue(false)
    }

    private fun handleGetUsersResult(result: GetUsersByTagUseCaseResult) {
        when (result) {
            is GetUsersByTagUseCaseResult.Success ->
                _users.postValue(result.users)

            is GetUsersByTagUseCaseResult.GenericError ->
                sendUsersError(result.error.code, result.error.message)

            is GetUsersByTagUseCaseResult.NetworkError ->
                sendUsersError(result.error.code, result.error.message)
        }
        _showLoader.postValue(false)
    }

    private fun sendPostsError(code: Int, message: String) {
        _postsErrors.postValue("Posts error code: $code - $message")
    }

    private fun sendUsersError(code: Int, message: String) {
        _usersErrors.postValue("Users error code: $code - $message")
    }

    private fun sendTagsOrInterestsError(code: Int, message: String) {
        _tagsOrInterestsErrors.postValue("Tags error code: $code - $message")
    }
}