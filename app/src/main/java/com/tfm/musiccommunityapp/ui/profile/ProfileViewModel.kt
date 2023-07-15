package com.tfm.musiccommunityapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowersUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowersUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowingUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetFollowingUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetUserProfileUseCase
import com.tfm.musiccommunityapp.domain.interactor.userprofile.GetUserProfileUseCaseResult
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.domain.model.UserDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserInfo: GetUserProfileUseCase,
    private val getUserFollowers: GetFollowersUseCase,
    private val getUserFollowing: GetFollowingUseCase
) : ViewModel() {

    private val _userInfo: MutableLiveData<UserDomain?> = MutableLiveData()
    private val _followers: MutableLiveData<List<ShortUserDomain>> = MutableLiveData()
    private val _following: MutableLiveData<List<ShortUserDomain>> = MutableLiveData()

    fun getUserInfo() = _userInfo as LiveData<UserDomain?>
    fun getFollowers() = _followers as LiveData<List<ShortUserDomain>>
    fun getFollowing() = _following as LiveData<List<ShortUserDomain>>


    fun setUp() {
        viewModelScope.launch(Dispatchers.IO) {

            handleGetUserInfoResult(getUserInfo(null))

            handleGetFollowersResult(getUserFollowers(null))

            handleGetFollowingResult(getUserFollowing())
        }
    }

    private fun handleGetUserInfoResult(result: GetUserProfileUseCaseResult) {
        when (result) {
            is GetUserProfileUseCaseResult.Success -> {
                _userInfo.postValue(result.user)
            }

            is GetUserProfileUseCaseResult.GenericError -> TODO()
            is GetUserProfileUseCaseResult.NetworkError -> TODO()
            is GetUserProfileUseCaseResult.NotFoundError -> TODO()
        }
    }

    private fun handleGetFollowersResult(result: GetFollowersUseCaseResult) {
        when (result) {
            is GetFollowersUseCaseResult.Success -> {
                _followers.postValue(result.followers)
            }

            is GetFollowersUseCaseResult.GenericError -> TODO()
            is GetFollowersUseCaseResult.NetworkError -> TODO()
            is GetFollowersUseCaseResult.NotFoundError -> TODO()
        }
    }

    private fun handleGetFollowingResult(result: GetFollowingUseCaseResult) {
        when (result) {
            is GetFollowingUseCaseResult.Success -> {
                _following.postValue(result.following)
            }

            is GetFollowingUseCaseResult.GenericError -> TODO()
            is GetFollowingUseCaseResult.NetworkError -> TODO()
        }
    }

}