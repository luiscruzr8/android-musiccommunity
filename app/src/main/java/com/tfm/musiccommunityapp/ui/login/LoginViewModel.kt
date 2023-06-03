package com.tfm.musiccommunityapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.login.SignInUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.SignInUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.login.SignOutUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.login.SignUpUseCase
import com.tfm.musiccommunityapp.domain.interactor.login.SignUpUseCaseResult
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
   //TODO check how to send dispatcher here: private val dispatcher: Dispatcher
): ViewModel() {

    private val _signInResult = SingleLiveEvent<SignInUseCaseResult?>()
    private val _signUpResult = SingleLiveEvent<SignUpUseCaseResult?>()
    private val _signOutResult = SingleLiveEvent<SignOutUseCaseResult?>()

    fun getSignInResult() = _signInResult as LiveData<SignInUseCaseResult?>
    fun getSignUpResult() = _signUpResult as LiveData<SignUpUseCaseResult?>
    fun getSignOutResult() = _signOutResult as LiveData<SignOutUseCaseResult?>

    fun performSignIn(username: String, password: String) {
        Log.d("LoginViewModel", "performSignIn")
        viewModelScope.launch(Dispatchers.IO) {
            _signInResult.postValue(signInUseCase(username, password))
        }
    }

    fun performSignUp(username: String, password: String, email: String, phone: String) {
        Log.d("LoginViewModel", "performSignUp")
        viewModelScope.launch(Dispatchers.IO) {
            _signUpResult.postValue(signUpUseCase(username, password, email, phone))
        }
    }

}