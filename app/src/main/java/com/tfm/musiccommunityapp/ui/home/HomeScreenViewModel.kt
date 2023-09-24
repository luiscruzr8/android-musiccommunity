package com.tfm.musiccommunityapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.model.UserDomain
import com.tfm.musiccommunityapp.usecase.login.GetCurrentUserResult
import com.tfm.musiccommunityapp.usecase.login.GetCurrentUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _currentUser: MutableLiveData<UserDomain?> = MutableLiveData()

    fun getCurrentUserLiveData() = _currentUser as LiveData<UserDomain?>

    fun setUp() {
        viewModelScope.launch(dispatcher) {
            handleGetCurrentUserResult(getCurrentUser())
        }
    }

    private fun handleGetCurrentUserResult(result: GetCurrentUserResult) {
        when (result) {
            is GetCurrentUserResult.Success -> {
                _currentUser.postValue(result.user)
            }

            GetCurrentUserResult.NoUser -> {
                _currentUser.postValue(null)
            }
        }
    }
}