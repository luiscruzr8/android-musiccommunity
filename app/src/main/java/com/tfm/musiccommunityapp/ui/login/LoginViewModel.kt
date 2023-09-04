package com.tfm.musiccommunityapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.ui.utils.SingleLiveEvent
import com.tfm.musiccommunityapp.usecase.login.SignInUseCase
import com.tfm.musiccommunityapp.usecase.login.SignInUseCaseResult
import com.tfm.musiccommunityapp.usecase.login.SignUpUseCase
import com.tfm.musiccommunityapp.usecase.login.SignUpUseCaseResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val showLoader = SingleLiveEvent<Boolean>()
    fun getShowLoader() = showLoader as LiveData<Boolean>

    private val _signInResult = SingleLiveEvent<SignInUseCaseResult?>()
    private val _signUpResult = SingleLiveEvent<SignUpUseCaseResult?>()
    fun getSignInResult() = _signInResult as LiveData<SignInUseCaseResult?>
    fun getSignUpResult() = _signUpResult as LiveData<SignUpUseCaseResult?>

    fun performSignIn(username: String, password: String) {
        showLoader.postValue(true)
        viewModelScope.launch(dispatcher) {
            _signInResult.postValue(signInUseCase(username, password))
            showLoader.postValue(false)
        }
    }

    fun performSignUp(username: String, password: String, email: String, phone: String) {
        showLoader.postValue(true)
        viewModelScope.launch(dispatcher) {
            _signUpResult.postValue(signUpUseCase(username, password, email, phone))
            showLoader.postValue(false)
        }
    }

}