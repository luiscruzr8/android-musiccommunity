package com.tfm.musiccommunityapp.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.LoginFragmentBinding
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment(R.layout.login_fragment) {

    private val binding by viewBinding(LoginFragmentBinding::bind)

    private val viewModel by viewModel<LoginViewModel>()

    private var signUpClicked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            submitButton.setOnClickListener { if (signUpClicked) submitSignUp() else submitLogIn() }

            swapBetweenLoginAndRegisterButton.setOnClickListener {
                resetValidations()
                signUpClicked = !signUpClicked

                loginSwapScreenTitle.text = if (signUpClicked) getString(R.string.login_screen_signup_title) else getString(R.string.login_screen_login_title)
                submitButton.text = if (signUpClicked) getString(R.string.login_screen_signup_button) else getString(R.string.login_screen_login_button)
                swapBetweenLoginAndRegisterButton.text = if (signUpClicked) getString(R.string.login_screen_login_helper) else getString(R.string.login_screen_signup_helper)

                emailTextInputLayout.isVisible = signUpClicked
                phoneNumberTextInputLayout.isVisible = signUpClicked
                if (signUpClicked) {
                    emailEditText.text = null
                    phoneNumberEditText.text = null
                }
            }
        }
    }

    private fun resetValidations() {
        binding.apply {
            usernameTextInputLayout.error = null
            passwordTextInputLayout.error = null
            emailTextInputLayout.error = null
            phoneNumberTextInputLayout.error = null
        }
    }

    private fun validateLogIn(): Boolean {
        var isValid = true

        if (binding.usernameEditText.text.isNullOrBlank()) {
            binding.usernameTextInputLayout.error = getString(R.string.login_screen_username_required)
            isValid = false
        } else {
            binding.usernameTextInputLayout.error = null
        }

        if (binding.passwordEditText.text.isNullOrBlank()) {
            binding.passwordTextInputLayout.error = getString(R.string.login_screen_password_required)
            isValid = false
        } else {
            binding.passwordTextInputLayout.error = null
        }

        return isValid
    }

    private fun submitLogIn() {
        if(validateLogIn()) {
            viewModel.performLogin()
        } else return
    }

    private fun validateSignUp(): Boolean {
        var isValid = true

        if (binding.emailEditText.text.isNullOrBlank()) {
            binding.emailTextInputLayout.error = getString(R.string.login_screen_email_required)
            isValid = false
        } else {
            binding.emailTextInputLayout.error = null
        }

        if (binding.phoneNumberEditText.text.isNullOrBlank()) {
            binding.phoneNumberTextInputLayout.error = getString(R.string.login_screen_phone_number_required)
            isValid = false
        } else {
            binding.phoneNumberTextInputLayout.error = null
        }

        return validateLogIn() && isValid
    }

    private fun submitSignUp() {
        if(validateSignUp()) {
            viewModel.performSignUp()
        } else return
    }

}