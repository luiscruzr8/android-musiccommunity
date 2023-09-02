package com.tfm.musiccommunityapp.ui.dialogs.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.EditProfileDialogBinding
import com.tfm.musiccommunityapp.domain.model.TagDomain
import com.tfm.musiccommunityapp.domain.model.UserDomain
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogTwoOptions
import java.util.regex.Pattern

class EditProfileDialog(
    private val userProfile: UserDomain,
    private val onSaveClicked: (UserDomain) -> Unit
) : DialogFragment() {

    private var _binding: EditProfileDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout to use as dialog or embedded fragment
        val dialogLayout = inflater.inflate(R.layout.edit_profile_dialog, container, false)
        _binding = EditProfileDialogBinding.bind(dialogLayout)

        setLayout(userProfile)

        return dialogLayout
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // the content
        val root = RelativeLayout(activity)
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // creating the fullscreen dialog
        val dialog = Dialog(root.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun extractUpdatedValues(): UserDomain {
        return userProfile.copy(
            phone = binding.phoneNumberEditText.text.toString().trim(),
            bio = binding.bioEditText.text.toString().trim(),
            interests = binding.userInterestsEditText.text.toString().trim().split(", ").map { TagDomain(it) }
        )
    }

    private fun setLayout(userProfile: UserDomain) {
        userProfile.apply {
            binding.usernameEditText.setText(login)
            binding.usernameTextInputLayout.isEnabled = false
            binding.emailEditText.setText(email)
            binding.emailTextInputLayout.isEnabled = false
            binding.phoneNumberEditText.setText(phone)
            binding.bioEditText.setText(bio)
            binding.userInterestsEditText.setText(interests.map { it2 -> it2.tagName }.joinToString(", "))
        }

        binding.dismissButton.setOnClickListener {
            dismiss()
        }

        binding.saveButton.setOnClickListener {
            if(!validateUpdatedValues()) {
                return@setOnClickListener
            }
            alertDialogTwoOptions(requireContext(),
                getString(R.string.edit_screen_save_dialog_title),
                null,
                getString(R.string.edit_screen_save_dialog_message),
                getString(R.string.accept),
                {
                    onSaveClicked(extractUpdatedValues()).run {
                        dismiss()
                    }
                },
                getString(R.string.cancel),
                { dismiss() }
            )
        }
    }

    private fun validateUpdatedValues(): Boolean {
        var isValid = true
        //Phone number validation
        if (binding.phoneNumberEditText.text.isNullOrBlank()) {
            binding.phoneNumberTextInputLayout.error = getString(R.string.edit_screen_phone_number_required)
            isValid = false
        } else if (!isValidPhoneNumber(binding.phoneNumberEditText.text.toString())) {
            binding.phoneNumberTextInputLayout.error = getString(R.string.edit_screen_phone_number_incorrect_format)
            isValid = false
        } else {
            binding.phoneNumberTextInputLayout.error = null
        }
        //Interests validation
        val interests = binding.userInterestsEditText.text.toString()
        if (interests.isNotEmpty() && !Pattern.matches("^[a-zA-Z0-9, áéíóúÁÉÍÓÚüÜñÑ]*\$", interests)) {
            binding.userInterestsInputLayout.error = getString(R.string.error_invalid_format_field)
            isValid = false
        } else {
            binding.userInterestsInputLayout.error = null
        }
        return isValid
    }

    private fun isValidPhoneNumber(text: String): Boolean {
        val phonePattern: Pattern = Pattern.compile(PHONE_NUMBER_REGEX)
        return phonePattern.matcher(text).matches()
    }

    companion object {
        const val PHONE_NUMBER_REGEX = "^[0-9]{6,9}\$"
    }

}