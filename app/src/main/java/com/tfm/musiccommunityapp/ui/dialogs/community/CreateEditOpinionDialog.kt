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
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.EditOpinionDialogBinding
import com.tfm.musiccommunityapp.domain.model.OpinionDomain
import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import com.tfm.musiccommunityapp.domain.model.TagDomain
import java.time.LocalDateTime
import java.util.regex.Pattern

class CreateEditOpinionDialog(
    private val opinion: OpinionDomain?,
    private val scores: List<ScoreDomain>,
    private val onSaveClicked: (OpinionDomain) -> Unit
) : DialogFragment() {

    private var _binding: EditOpinionDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogLayout = inflater.inflate(R.layout.edit_opinion_dialog, container, false)
        _binding = EditOpinionDialogBinding.bind(dialogLayout)

        setLayout(opinion)

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
            val title = postTitleEditText.text.toString().trim()
            val description = postDescriptionEditText.text.toString().trim()
            val score = opinionScoreEditText.text.toString().trim()
            val tags = postTagsEditText.text.toString().trim()

            if (title.isEmpty()) {
                postTitle.error = getString(R.string.error_empty_field)
                isValid = false
            } else postTitle.error = null

            if (description.isEmpty()) {
                postDescription.error = getString(R.string.error_empty_field)
                isValid = false
            } else postDescription.error = null

            if (score.isEmpty()) {
                opinionScore.error = getString(R.string.error_empty_field)
                isValid = false
            } else opinionScore.error = null

            if (tags.isNotEmpty() && !Pattern.matches("^[a-zA-Z0-9, áéíóúÁÉÍÓÚüÜñÑ]*\$", tags)) {
                postTags.error = getString(R.string.error_invalid_format_field)
                isValid = false
            } else postTags.error = null
        }
        return isValid
    }

    private fun extractValues(): OpinionDomain {
        binding.apply {
            val selectedScore = scores.firstOrNull {
                it.name == opinionScoreEditText.text.toString().trim()
            } ?: ScoreDomain(
                0L,
                opinionScoreEditText.text.toString().trim(),
                "",
                LocalDateTime.now()
            )

            return opinion?.copy(
                title = postTitleEditText.text.toString().trim(),
                description = postDescriptionEditText.text.toString().trim(),
                tags = postTagsEditText.text.toString().trim().split(", ").map { TagDomain(it) },
            ) ?: OpinionDomain(
                id = 0L,
                login = "",
                title = postTitleEditText.text.toString().trim(),
                scoreId = selectedScore.id,
                score = selectedScore,
                description = postDescriptionEditText.text.toString().trim(),
                tags = postTagsEditText.text.toString().trim().split(", ").map { TagDomain(it) },
                postType = getString(R.string.event),
                createdOn = LocalDateTime.now()
            )
        }
    }

    private fun setLayout(opinion: OpinionDomain?) {
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

            (opinionScoreEditText as? MaterialAutoCompleteTextView)?.setSimpleItems(scores.map { it.name }.toTypedArray())
            opinion?.let {
                dialogTitle.text = getString(R.string.community_opinion_edit_title)
                postTitleEditText.setText(it.title)
                postDescriptionEditText.setText(it.description)
                opinionScoreEditText.setText(it.score.name, false)
                postTagsEditText.setText(it.tags.map{ tag -> tag.tagName }.joinToString(", ") )
            }
        }
    }
}