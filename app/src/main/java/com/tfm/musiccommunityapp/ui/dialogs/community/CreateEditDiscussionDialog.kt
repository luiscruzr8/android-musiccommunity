package com.tfm.musiccommunityapp.ui.dialogs.community

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
import com.tfm.musiccommunityapp.databinding.EditDiscussionDialogBinding
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.domain.model.TagDomain
import java.time.LocalDateTime
import java.util.regex.Pattern

class CreateEditDiscussionDialog(
    private val discussion: DiscussionDomain?,
    private val onSaveClicked: (DiscussionDomain) -> Unit
) : DialogFragment(){

    private var _binding: EditDiscussionDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogLayout = inflater.inflate(R.layout.edit_discussion_dialog, container, false)
        _binding = EditDiscussionDialogBinding.bind(dialogLayout)

        setLayout(discussion)

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
            val tags = postTagsEditText.text.toString()

            if (title.isEmpty()) {
                postTitle.error = getString(R.string.error_empty_field)
                isValid = false
            } else postTitle.error = null

            if (description.isEmpty()) {
                postDescription.error = getString(R.string.error_empty_field)
                isValid = false
            } else postDescription.error = null

            if (tags.isNotEmpty() && !Pattern.matches("^[a-zA-Z0-9, ]*$", tags)) {
                postTags.error = getString(R.string.error_invalid_format_field)
                isValid = false
            } else postTags.error = null
        }
        return isValid
    }

    private fun extractValues(): DiscussionDomain {
        binding.apply {
            return discussion?.copy(
                title = postTitleEditText.text.toString().trim(),
                description = postDescriptionEditText.text.toString().trim(),
                tags = postTagsEditText.text.toString().trim().split(", ").map { TagDomain(it) },
            ) ?: DiscussionDomain(
                id = 0L,
                login = "",
                title = postTitleEditText.text.toString().trim(),
                description = postDescriptionEditText.text.toString().trim(),
                tags = postTagsEditText.text.toString().trim().split(", ").map { TagDomain(it) },
                postType = getString(R.string.advertisement),
                createdOn = LocalDateTime.now()
            )
        }
    }

    private fun setLayout(discussion: DiscussionDomain?) {
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

            discussion?.let {
                dialogTitle.text = getString(R.string.community_advertisements_edit_title)
                postTitleEditText.setText(it.title)
                postDescriptionEditText.setText(it.description)
                postTagsEditText.setText(it.tags.map { it2 -> it2.tagName }.joinToString(", "))
            }
        }
    }
}