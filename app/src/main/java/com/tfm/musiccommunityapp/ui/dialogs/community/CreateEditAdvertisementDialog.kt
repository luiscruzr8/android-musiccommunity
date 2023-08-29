package com.tfm.musiccommunityapp.ui.dialogs.community

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.EditAdvertisementDialogBinding
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.domain.model.TagDomain
import com.tfm.musiccommunityapp.utils.formatDateToString
import com.tfm.musiccommunityapp.utils.formatTimeToString
import com.tfm.musiccommunityapp.utils.localDateOf
import com.tfm.musiccommunityapp.utils.localTimeOf
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.regex.Pattern

class CreateEditAdvertisementDialog(
    private val advertisement: AdvertisementDomain?,
    private val cities: List<CityDomain>,
    private val onSaveClicked: (AdvertisementDomain) -> Unit
) : DialogFragment() {

    private var _binding: EditAdvertisementDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogLayout = inflater.inflate(R.layout.edit_advertisement_dialog, container, false)
        _binding = EditAdvertisementDialogBinding.bind(dialogLayout)

        setLayout(advertisement)

        return dialogLayout
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
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

    private fun validate(): Boolean {
        var isValid = true
        binding.apply {
            val title = postTitleEditText.text.toString()
            val description = postDescriptionEditText.text.toString()
            val phone = advertisementPhoneNumberEditText.text.toString()
            val location = postLocationEditText.text.toString()
            val untilDate = advertisementUntilDateEditText.text.toString()
            val tags = postTagsEditText.text.toString()

            if (title.isEmpty()) {
                postTitle.error = getString(R.string.error_empty_field)
                isValid = false
            } else postTitle.error = null

            if (description.isEmpty()) {
                postDescription.error = getString(R.string.error_empty_field)
                isValid = false
            } else postDescription.error = null

            if (phone.isEmpty()) {
                advertisementPhoneNumber.error = getString(R.string.error_empty_field)
                isValid = false
            } else advertisementPhoneNumber.error = null

            if (location.isEmpty()) {
                postLocation.error = getString(R.string.error_empty_field)
                isValid = false
            } else postLocation.error = null

            if (untilDate.isEmpty()) {
                advertisementUntilDate.error = getString(R.string.error_empty_field)
                isValid = false
            } else advertisementUntilDate.error = null

            if (tags.isNotEmpty() && !Pattern.matches("^[a-zA-Z0-9, ]*$", tags)) {
                postTags.error = getString(R.string.error_invalid_format_field)
                isValid = false
            } else postTags.error = null
        }
        return isValid
    }

    private fun extractValues(): AdvertisementDomain {
        binding.apply {
            val selectedUntilDate = advertisementUntilDateEditText.text.toString().trim().localDateOf()

            return advertisement?.copy(
                title = postTitleEditText.text.toString().trim(),
                cityName = postLocationEditText.text.toString().trim(),
                description = postDescriptionEditText.text.toString().trim(),
                phone = advertisementPhoneNumberEditText.text.toString().trim(),
                until = selectedUntilDate,
                tags = postTagsEditText.text.toString().trim().split(", ").map { TagDomain(it) },
            ) ?: AdvertisementDomain(
                id = 0L,
                login = "",
                title = postTitleEditText.text.toString().trim(),
                cityName = postLocationEditText.text.toString().trim(),
                description = postDescriptionEditText.text.toString().trim(),
                phone = advertisementPhoneNumberEditText.text.toString().trim(),
                until = selectedUntilDate,
                tags = postTagsEditText.text.toString().trim().split(", ").map { TagDomain(it) },
                postType = getString(R.string.advertisement),
                createdOn = LocalDateTime.now()
            )
        }
    }

    private fun setLayout(advertisement: AdvertisementDomain?) {
        binding.apply {
            saveButton.setOnClickListener {
                if(!validate()) {
                    return@setOnClickListener
                }
                onSaveClicked(extractValues()).run {
                    dismiss()
                }
            }
            dismissButton.setOnClickListener {
                dismiss()
            }

            advertisementUntilDateEditText.setOnClickListener { showDatePicker(advertisementUntilDateEditText) }
            (postLocationEditText as? MaterialAutoCompleteTextView)?.setSimpleItems(cities.map { it.cityName }.toTypedArray())

            advertisement?.let {
                dialogTitle.text = getString(R.string.community_advertisements_edit_title)
                postTitleEditText.setText(it.title)
                postDescriptionEditText.setText(it.description)
                advertisementPhoneNumberEditText.setText(it.phone)
                postLocationEditText.setText(it.cityName, false)
                advertisementUntilDateEditText.setText(it.until.formatDateToString())
                postTagsEditText.setText(it.tags.map { it2 -> it2.tagName }.joinToString(", "))
            }
        }
    }

    private fun showDatePicker(editText: EditText) {
        val today = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000
        val tomorrow = today + (24 * 60 * 60 * 1000)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.date_hint))
            .setSelection(tomorrow)
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val aux = Instant.ofEpochMilli(selection)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            editText.setText(aux.formatDateToString())
        }

        datePicker.show(parentFragmentManager, "DatePicker")
    }

}