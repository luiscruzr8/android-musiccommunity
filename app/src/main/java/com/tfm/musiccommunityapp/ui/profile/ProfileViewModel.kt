package com.tfm.musiccommunityapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.login.SignOutUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.SignOutUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowersUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowersUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowingUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowingUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetUserProfileUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetUserProfileUseCaseResult
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.domain.model.UserDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserInfo: GetUserProfileUseCase,
    private val getUserFollowers: GetFollowersUseCase,
    private val getUserFollowing: GetFollowingUseCase,
    private val signOut: SignOutUseCase
) : ViewModel() {

    private val _userInfo: MutableLiveData<UserDomain?> = MutableLiveData()
    private val _followers: MutableLiveData<List<ShortUserDomain>> = MutableLiveData()
    private val _following: MutableLiveData<List<ShortUserDomain>> = MutableLiveData()

    private val _errorProfile: MutableLiveData<String> = MutableLiveData()

    private val _signOutSuccess: SingleLiveEvent<Unit> = SingleLiveEvent()

    fun getUserInfo() = _userInfo as LiveData<UserDomain?>
    fun getFollowers() = _followers as LiveData<List<ShortUserDomain>>
    fun getFollowing() = _following as LiveData<List<ShortUserDomain>>
    fun getSignOutSuccess() = _signOutSuccess as LiveData<Unit>
    fun getErrorProfile() = _errorProfile as LiveData<String>

    private val showLoader = SingleLiveEvent<Boolean>()
    fun getShowLoader() = showLoader as LiveData<Boolean>

    fun setUp() {
        viewModelScope.launch(Dispatchers.IO) {
            showLoader.postValue(true)

            handleGetUserInfoResult(getUserInfo(null))

            handleGetFollowersResult(getUserFollowers(null))

            handleGetFollowingResult(getUserFollowing())

            showLoader.postValue(false)
        }
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            handleSignOutResult(signOut.invoke())
        }
    }

    private fun handleGetUserInfoResult(result: GetUserProfileUseCaseResult) {
        when (result) {
            is GetUserProfileUseCaseResult.Success -> {
                _userInfo.postValue(result.user)
            }

            is GetUserProfileUseCaseResult.GenericError ->
                sendProfileError(result.error.code, result.error.message)

            is GetUserProfileUseCaseResult.NetworkError ->
                sendProfileError(result.error.code, result.error.message)

            is GetUserProfileUseCaseResult.NotFoundError ->
                sendProfileError(result.error.code, result.error.message)
        }
    }

    private fun handleGetFollowersResult(result: GetFollowersUseCaseResult) {
        when (result) {
            is GetFollowersUseCaseResult.Success -> {
                _followers.postValue(result.followers)
            }

            is GetFollowersUseCaseResult.GenericError ->
                sendProfileError(result.error.code, result.error.message)

            is GetFollowersUseCaseResult.NetworkError ->
                sendProfileError(result.error.code, result.error.message)

            is GetFollowersUseCaseResult.NotFoundError ->
                sendProfileError(result.error.code, result.error.message)
        }
    }

    private fun handleGetFollowingResult(result: GetFollowingUseCaseResult) {
        when (result) {
            is GetFollowingUseCaseResult.Success -> {
                _following.postValue(result.following)
            }

            is GetFollowingUseCaseResult.GenericError ->
                sendProfileError(result.error.code, result.error.message)

            is GetFollowingUseCaseResult.NetworkError ->
                sendProfileError(result.error.code, result.error.message)
        }
    }

    private fun handleSignOutResult(result: SignOutUseCaseResult) {
        when (result) {
            is SignOutUseCaseResult.Success -> {
                _signOutSuccess.postValue(Unit)
            }
        }
    }

    private fun sendProfileError(code: Int, message: String) {
        _errorProfile.postValue("Error code: $code - $message")
    }
}