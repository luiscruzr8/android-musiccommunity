package com.tfm.musiccommunityapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserResult
import com.tfm.musiccommunityapp.domain.interactor.login.GetCurrentUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.SignOutUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.SignOutUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.userprofile.FollowUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.FollowUserUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowersUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowersUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowingUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowingUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetUserProfileUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetUserProfileUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.userprofile.IsUserFollowerOfUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.IsUserFollowerOfUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.userprofile.UnfollowUserUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.UnfollowUserUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.userprofile.UpdateUserProfileUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.UpdateUserProfileUseCaseResult
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.domain.model.UserDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserInfo: GetUserProfileUseCase,
    private val getUserFollowers: GetFollowersUseCase,
    private val getUserFollowing: GetFollowingUseCase,
    private val updateProfile: UpdateUserProfileUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val isUserFollower: IsUserFollowerOfUseCase,
    private val followUser: FollowUserUseCase,
    private val unfollowUser: UnfollowUserUseCase,
    private val signOut: SignOutUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    enum class UserOperationSuccess { FOLLOW, UNFOLLOW }

    private val _userInfo: MutableLiveData<UserDomain?> = MutableLiveData()
    private val _followers: MutableLiveData<List<ShortUserDomain>> = MutableLiveData()
    private val _following: MutableLiveData<List<ShortUserDomain>> = MutableLiveData()
    private val _errorProfile: MutableLiveData<String> = MutableLiveData()
    private val _signOutSuccess: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _isMyProfile: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _isUserFollower: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _isOperationSuccessful: SingleLiveEvent<UserOperationSuccess> =
        SingleLiveEvent()

    fun getUserInfo() = _userInfo as LiveData<UserDomain?>
    fun getFollowers() = _followers as LiveData<List<ShortUserDomain>>
    fun getFollowing() = _following as LiveData<List<ShortUserDomain>>
    fun getSignOutSuccess() = _signOutSuccess as LiveData<Unit>
    fun getErrorProfile() = _errorProfile as LiveData<String>
    fun isMyProfileLiveData() = _isMyProfile as LiveData<Boolean>
    fun isUserFollowerLiveData() = _isUserFollower as LiveData<Boolean>
    fun isOperationSuccessfulLiveData() =
        _isOperationSuccessful as LiveData<UserOperationSuccess>

    private val showLoader = SingleLiveEvent<Boolean>()
    fun getShowLoader() = showLoader as LiveData<Boolean>

    fun setUp(username: String?) {
        viewModelScope.launch(dispatcher) {
            showLoader.postValue(true)

            handleGetUserInfoResult(getUserInfo(username)).run {
                handleGetCurrentUserResult(getCurrentUser())
            }
            username?.let { handleIsUserFollower(isUserFollower(it)) }
            handleGetFollowersResult(getUserFollowers(username))
            handleGetFollowingResult(getUserFollowing())

            showLoader.postValue(false)
        }
    }

    private fun handleGetCurrentUserResult(result: GetCurrentUserResult) {
        when (result) {
            is GetCurrentUserResult.Success -> {
                _isMyProfile.postValue(result.user?.let {
                    _userInfo.value?.login == it.login
                } ?: false)
            }
            GetCurrentUserResult.NoUser -> _isMyProfile.postValue(false)
        }
    }

    private fun handleIsUserFollower(result: IsUserFollowerOfUseCaseResult) {
        when (result) {
            is IsUserFollowerOfUseCaseResult.Success -> {
                _isUserFollower.postValue(result.isFollower)
            }
            else -> _isUserFollower.postValue(false)
        }
    }

    fun signOut() {
        viewModelScope.launch(dispatcher) {
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

    private fun handleUpdateProfileResult(result: UpdateUserProfileUseCaseResult) {
        when (result) {
            is UpdateUserProfileUseCaseResult.Success -> {
                _userInfo.postValue(result.user)
            }

            is UpdateUserProfileUseCaseResult.NotFoundError ->
                sendProfileError(result.error.code, result.error.message)

            is UpdateUserProfileUseCaseResult.GenericError ->
                sendProfileError(result.error.code, result.error.message)

            is UpdateUserProfileUseCaseResult.NetworkError ->
                sendProfileError(result.error.code, result.error.message)
        }
    }

    private fun sendProfileError(code: Int, message: String) {
        _errorProfile.postValue("Error code: $code - $message")
    }

    fun sendUpdateProfile(updatedProfile: UserDomain) {
        viewModelScope.launch(dispatcher) {
            showLoader.postValue(true)
            handleUpdateProfileResult(updateProfile(updatedProfile)).run {
                showLoader.postValue(
                    false
                )
            }
        }
    }

    private fun handleFollowUserResult(result: FollowUserUseCaseResult) {
        when(result) {
            is FollowUserUseCaseResult.Success ->
                _isOperationSuccessful.postValue(UserOperationSuccess.FOLLOW)

            is FollowUserUseCaseResult.GenericError ->
                _errorProfile.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is FollowUserUseCaseResult.NetworkError ->
                _errorProfile.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is FollowUserUseCaseResult.NotFoundError ->
                _errorProfile.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    private fun handleUnfollowUserResult(result: UnfollowUserUseCaseResult) {
        when(result) {
            is UnfollowUserUseCaseResult.Success ->
                _isOperationSuccessful.postValue(UserOperationSuccess.FOLLOW)

            is UnfollowUserUseCaseResult.GenericError ->
                _errorProfile.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is UnfollowUserUseCaseResult.NetworkError ->
                _errorProfile.postValue("Error code: ${result.error.code} - ${result.error.message}")

            is UnfollowUserUseCaseResult.NotFoundError ->
                _errorProfile.postValue("Error code: ${result.error.code} - ${result.error.message}")
        }
    }

    fun sendFollowUser(username: String) {
        viewModelScope.launch(dispatcher) {
            showLoader.postValue(true)
            handleFollowUserResult(followUser(username))
        }
    }

    fun sendUnfollowUser(username: String) {
        viewModelScope.launch(dispatcher) {
            showLoader.postValue(true)
            handleUnfollowUserResult(unfollowUser(username))
        }
    }
}