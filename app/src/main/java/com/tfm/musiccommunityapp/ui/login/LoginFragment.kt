package com.tfm.musiccommunityapp.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.LoginFragmentBinding
import com.tfm.musiccommunityapp.domain.interactor.login.SignInUseCaseResult
import com.tfm.musiccommunityapp.domain.interactor.login.SignUpUseCaseResult
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.regex.Pattern

class LoginFragment : BaseFragment(R.layout.login_fragment) {

    private val binding by viewBinding(LoginFragmentBinding::bind)

    private val viewModel by viewModel<LoginViewModel>()

    private var signUpClicked = false

    private fun observeLoader() {
        viewModel.getShowLoader().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeSignInResult() {
        viewModel.getSignInResult().observe(viewLifecycleOwner) { signInResult ->
            when (signInResult) {
                is SignInUseCaseResult.Success -> {
                    clearFields()
                    navigateToUserProfile()
                }

                is SignInUseCaseResult.GenericError -> {
                    alertDialogOneOption(
                        requireContext(),
                        getString(R.string.login_screen_error_sign_in_title),
                        null,
                        "${signInResult.error.message} (Error ${signInResult.error.code})",
                        getString(R.string.ok),
                        null
                    )
                }
                null -> {
                    //Nothing to do here
                }
            }
        }
    }

    private fun observeSignUpResult() {
        viewModel.getSignUpResult().observe(viewLifecycleOwner) { signUpResult ->
            when (signUpResult) {
                is SignUpUseCaseResult.Success -> {
                    alertDialogOneOption(
                        requireContext(),
                        getString(R.string.login_screen_success_sign_up_title),
                        null,
                        getString(R.string.login_screen_success_sign_up_message),
                        getString(R.string.ok),
                        null
                    )
                    swapBetweenLoginAndRegisterAction(true)
                }
                
                is SignUpUseCaseResult.GenericError -> {
                    alertDialogOneOption(
                        requireContext(),
                        getString(R.string.login_screen_error_sign_up_title),
                        null,
                        "${signUpResult.error.message} (Error ${signUpResult.error.code})",
                        getString(R.string.ok),
                        null
                    )
                }

                null -> {
                    //Nothing to do here
                }
            }
        }
    }

    private fun navigateToUserProfile() {
        val direction = LoginFragmentDirections.actionLoginFragmentToProfileFragment()
        navigateSafe(direction)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLoader()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeSignInResult()
        observeSignUpResult()

        binding.apply {
            val progressIndicatorSpec = CircularProgressIndicatorSpec(requireContext(), null)
            val progressIndicator = IndeterminateDrawable.createCircularDrawable(
                requireContext(),
                progressIndicatorSpec
            )

            submitButton.setOnClickListener {
                submitButton.icon = progressIndicator
                if (signUpClicked) submitSignUp() else submitLogIn()
            }

            swapBetweenLoginAndRegisterButton.setOnClickListener {
                swapBetweenLoginAndRegisterAction(false)
            }
        }
    }

    private fun swapBetweenLoginAndRegisterAction(resetFields: Boolean) {

        binding.apply {
            if (resetFields) clearFields() else resetValidations()

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

    private fun clearFields() {
        binding.apply {
            usernameEditText.text = null
            passwordEditText.text = null
            emailEditText.text = null
            phoneNumberEditText.text = null
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
        //Username validation
        if (binding.usernameEditText.text.isNullOrBlank()) {
            binding.usernameTextInputLayout.error = getString(R.string.login_screen_username_required)
            isValid = false
        } else if (!isValidUsernameOrPassword(binding.usernameEditText.text.toString())) {
            binding.usernameTextInputLayout.error = getString(R.string.login_screen_username_incorrect_format)
            isValid = false
        } else {
            binding.usernameTextInputLayout.error = null
        }
        //Password validation
        if (binding.passwordEditText.text.isNullOrBlank()) {
            binding.passwordTextInputLayout.error = getString(R.string.login_screen_password_required)
            isValid = false
        } else if (!isValidUsernameOrPassword(binding.passwordEditText.text.toString())) {
            binding.passwordTextInputLayout.error = getString(R.string.login_screen_password_incorrect_format)
            isValid = false
        } else {
            binding.passwordTextInputLayout.error = null
        }

        return isValid
    }

    private fun submitLogIn() {
        if(validateLogIn()) {
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            viewModel.performSignIn(username, password)
        } else return
    }

    private fun validateSignUp(): Boolean {
        var isValid = true
        //Email validation
        if (binding.emailEditText.text.isNullOrBlank()) {
            binding.emailTextInputLayout.error = getString(R.string.login_screen_email_required)
            isValid = false
        } else if (!isValidEmail(binding.emailEditText.text.toString())) {
            binding.emailTextInputLayout.error = getString(R.string.login_screen_email_incorrect_format)
            isValid = false
        } else {
            binding.emailTextInputLayout.error = null
        }
        //Phone number validation
        if (binding.phoneNumberEditText.text.isNullOrBlank()) {
            binding.phoneNumberTextInputLayout.error = getString(R.string.login_screen_phone_number_required)
            isValid = false
        } else if (!isValidPhoneNumber(binding.phoneNumberEditText.text.toString())) {
            binding.phoneNumberTextInputLayout.error = getString(R.string.login_screen_phone_number_incorrect_format)
            isValid = false
        } else {
            binding.phoneNumberTextInputLayout.error = null
        }

        return validateLogIn() && isValid
    }

    private fun submitSignUp() {
        if(validateSignUp()) {
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val phone = binding.phoneNumberEditText.text.toString().trim()

            viewModel.performSignUp(username, password, email, phone)
        } else return
    }

    private fun isValidUsernameOrPassword(text: String): Boolean {
        val userNamePattern: Pattern = Pattern.compile(USERNAME_OR_PASSWORD_REGEX)
        return userNamePattern.matcher(text).matches()
    }

    private fun isValidEmail(text: String): Boolean {
        val emailPattern: Pattern = Pattern.compile(EMAIL_REGEX)
        return emailPattern.matcher(text).matches()
    }

    private fun isValidPhoneNumber(text: String): Boolean {
        val phonePattern: Pattern = Pattern.compile(PHONE_NUMBER_REGEX)
        return phonePattern.matcher(text).matches()
    }

    companion object {
        const val USERNAME_OR_PASSWORD_REGEX = "^[a-zA-Z0-9_]{6,40}$"
        const val EMAIL_REGEX = "^(?=.{6,60}\$)[a-zA-Z0-9_!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,6}\$"
        const val PHONE_NUMBER_REGEX = "^[0-9]{6,9}\$"
    }

}