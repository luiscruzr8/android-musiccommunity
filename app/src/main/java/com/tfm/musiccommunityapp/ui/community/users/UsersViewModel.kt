package com.tfm.musiccommunityapp.ui.community.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetUsersUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetUsersUseCaseResult
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class UsersViewModel(
    private val getUsers: GetUsersUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private val _users: MutableLiveData<List<ShortUserDomain>> = MutableLiveData()
    private val _communityUsersError: SingleLiveEvent<String> = SingleLiveEvent()
    private val showUsersLoader = MutableLiveData<Boolean>()

    fun getUsersLiveData() = _users as LiveData<List<ShortUserDomain>>
    fun getCommunityUsersError() = _communityUsersError as LiveData<String>
    fun isUsersLoading() = showUsersLoader as LiveData<Boolean>

    fun setUpUsers(searchTerm: String?) {
        viewModelScope.launch(dispatcher) {
            showUsersLoader.postValue(true)
            handleGetUsersResult(getUsers(searchTerm))
        }
    }

    private fun handleGetUsersResult(result: GetUsersUseCaseResult) {
        when (result) {
            is GetUsersUseCaseResult.Success -> {
                _users.postValue(result.users)
            }
            is GetUsersUseCaseResult.NetworkError -> {
                _users.postValue(emptyList())
                sendCommunityUsersError(result.error.code, result.error.message)
            }
            is GetUsersUseCaseResult.GenericError -> {
                _users.postValue(emptyList())
                sendCommunityUsersError(result.error.code, result.error.message)
            }
        }
        showUsersLoader.postValue(false)
    }

    private fun sendCommunityUsersError(code: Int, message: String) {
        _communityUsersError.postValue("Error code: $code - $message")
    }
}