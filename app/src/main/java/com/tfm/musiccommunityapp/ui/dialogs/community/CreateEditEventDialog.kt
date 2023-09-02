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
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.EditEventDialogBinding
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.EventDomain
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

class CreateEditEventDialog(
    private val event: EventDomain?,
    private val cities: List<CityDomain>,
    private val onSaveClicked: (EventDomain) -> Unit
) : DialogFragment() {

    private var _binding: EditEventDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogLayout = inflater.inflate(R.layout.edit_event_dialog, container, false)
        _binding = EditEventDialogBinding.bind(dialogLayout)

        setLayout(event)

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
            val location = postLocationEditText.text.toString()
            val fromDate = eventFromDateEditText.text.toString()
            val fromTime = eventFromTimeEditText.text.toString()
            val toDate = eventToDateEditText.text.toString()
            val toTime = eventToTimeEditText.text.toString()
            val tags = postTagsEditText.text.toString()

            if (title.isEmpty()) {
                postTitle.error = getString(R.string.error_empty_field)
                isValid = false
            } else postTitle.error = null

            if (description.isEmpty()) {
                postDescription.error = getString(R.string.error_empty_field)
                isValid = false
            } else postDescription.error = null

            if (location.isEmpty()) {
                postLocation.error = getString(R.string.error_empty_field)
                isValid = false
            } else postLocation.error = null

            if (fromDate.isEmpty()) {
                eventFromDate.error = getString(R.string.error_empty_field)
                isValid = false
            } else eventFromDate.error = null

            if (fromTime.isEmpty()) {
                eventFromTime.error = getString(R.string.error_empty_field)
                isValid = false
            } else eventFromTime.error = null

            if (toDate.isEmpty()) {
                eventToDate.error = getString(R.string.error_empty_field)
                isValid = false
            } else postDescription.error = null

            if (toTime.isEmpty()) {
                eventToTime.error = getString(R.string.error_empty_field)
                isValid = false
            } else eventToTime.error = null

            if (tags.isNotEmpty() && !Pattern.matches("^[a-zA-Z0-9, áéíóúÁÉÍÓÚüÜñÑ]*\$", tags)) {
                postTags.error = getString(R.string.error_invalid_format_field)
                isValid = false
            } else postTags.error = null
        }
        return isValid
    }

    private fun extractValues(): EventDomain {
        binding.apply {
            val selectedFromDate = eventFromDateEditText.text.toString().trim().localDateOf()
            val selectedFromTime = eventFromTimeEditText.text.toString().trim().localTimeOf()
            val fromDateTime = LocalDateTime.of(selectedFromDate, selectedFromTime)

            val selectedToDate = eventToDateEditText.text.toString().trim().localDateOf()
            val selectedToTime = eventToTimeEditText.text.toString().trim().localTimeOf()
            val toDateTime = LocalDateTime.of(selectedToDate, selectedToTime)

            return event?.copy(
                title = postTitleEditText.text.toString().trim(),
                cityName = postLocationEditText.text.toString().trim(),
                description = postDescriptionEditText.text.toString().trim(),
                from = fromDateTime,
                to = toDateTime,
                tags = postTagsEditText.text.toString().trim().split(", ").map { TagDomain(it) },
            ) ?: EventDomain(
                id = 0L,
                login = "",
                title = postTitleEditText.text.toString().trim(),
                cityName = postLocationEditText.text.toString().trim(),
                description = postDescriptionEditText.text.toString().trim(),
                from = fromDateTime,
                to = toDateTime,
                tags = postTagsEditText.text.toString().trim().split(", ").map { TagDomain(it) },
                postType = getString(R.string.event),
                createdOn = LocalDateTime.now()
            )
        }
    }

    private fun setLayout(event: EventDomain?) {
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

            eventFromDateEditText.setOnClickListener { showDatePicker(eventFromDateEditText) }
            eventFromTimeEditText.setOnClickListener { showTimePicker(eventFromTimeEditText) }
            eventToDateEditText.setOnClickListener { showDatePicker(eventToDateEditText) }
            eventToTimeEditText.setOnClickListener { showTimePicker(eventToTimeEditText) }
            (postLocationEditText as? MaterialAutoCompleteTextView)?.setSimpleItems(cities.map { it.cityName }.toTypedArray())

            event?.let {
                dialogTitle.text = getString(R.string.community_events_edit_title)
                postTitleEditText.setText(it.title)
                postDescriptionEditText.setText(it.description)
                postLocationEditText.setText(it.cityName, false)
                eventFromDateEditText.setText(it.from.formatDateToString())
                eventFromTimeEditText.setText(it.from.formatTimeToString())
                eventToDateEditText.setText(it.to.formatDateToString())
                eventToTimeEditText.setText(it.to.formatTimeToString())
                postTagsEditText.setText(it.tags.map { it2 -> it2.tagName }.joinToString(", "))
            }
        }
    }

    private fun showDatePicker(editText: EditText) {
        val today = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000
        val tomorrow = today + (24 * 60 * 60 * 1000)

        val datePicker = datePicker()
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

    private fun showTimePicker(
        editText: EditText
    ) {
        val timePicker = MaterialTimePicker.Builder()
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Time")
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val aux = LocalTime.of(timePicker.hour, timePicker.minute)
            editText.setText(aux.formatTimeToString())
        }

        timePicker.show(parentFragmentManager, "TimePicker")
    }

}