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
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.EditRecommendationDialogBinding
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.utils.formatDateToString
import com.tfm.musiccommunityapp.utils.getChipColor
import com.tfm.musiccommunityapp.utils.getChipLabel
import java.time.LocalDateTime

class CreateEditRecommendationDialog (
    private val recommendation: RecommendationDomain?,
    private val post: GenericPostDomain,
    private val onSaveClicked: (RecommendationDomain) -> Unit
): DialogFragment() {

    private var _binding: EditRecommendationDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogLayout = inflater.inflate(R.layout.edit_recommendation_dialog, container, false)
        _binding = EditRecommendationDialogBinding.bind(dialogLayout)

        setLayout(recommendation, post)

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
            val title = recommendationTitleEditText.text.toString()
            val message = recommendationMessageEditText.text.toString()

            if (title.isEmpty()) {
                recommendationTitle.error = getString(R.string.error_empty_field)
                isValid = false
            } else recommendationTitle.error = null

            if (message.isEmpty()) {
                recommendationMessage.error = getString(R.string.error_empty_field)
                isValid = false
            } else recommendationMessage.error = null
        }
        return isValid
    }

    private fun extractValues(): RecommendationDomain {
        binding.apply {
            return recommendation?.copy(
                    recommendationTitle = recommendationTitleEditText.text.toString().trim(),
                    recommendationMessage = recommendationMessageEditText.text.toString().trim(),
            ) ?: RecommendationDomain(
                id = 0,
                    login = "",
                recommendationTitle = recommendationTitleEditText.text.toString().trim(),
                recommendationMessage = recommendationMessageEditText.text.toString().trim(),
                rating = 0.0,
                postId = post.id,
                post = post,
                createdOn = LocalDateTime.now()
            )
        }
    }

    private fun setLayout(recommendation: RecommendationDomain?, post: GenericPostDomain) {
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

            recommendation?.let { item ->
                recommendationTitleEditText.setText(item.recommendationTitle)
                recommendationMessageEditText.setText(item.recommendationMessage)

                tvPostChip.chipBackgroundColor =
                    getChipColor(item.post.postType, requireContext())
                tvPostChip.text = String.format(
                        getString(R.string.chip_post),
                        item.post.id,
                        getChipLabel(item.post.postType, requireContext())
                )
                tvPostCreationDate.text = item.post.createdOn.formatDateToString()
                tvPostCreationUser.text = item.post.login
                tvPostTitle.text = item.post.title
                tvPostDescription.text = item.post.description
                if (item.post.tags.isNotEmpty()) {
                    tvTagsLabel.isVisible = true
                    relatedTagsLayout.isVisible = true
                    relatedTagsLayout.setTagList(item.post.tags.map { it2 -> it2.tagName })
                }
            } ?: run {
                tvPostChip.chipBackgroundColor =
                    getChipColor(post.postType, requireContext())
                tvPostChip.text = String.format(
                        getString(R.string.chip_post),
                        post.id,
                        getChipLabel(post.postType, requireContext())
                )
                tvPostCreationDate.text = post.createdOn.formatDateToString()
                tvPostCreationUser.text = post.login
                tvPostTitle.text = post.title
                tvPostDescription.text = post.description
                if (post.tags.isNotEmpty()) {
                    tvTagsLabel.isVisible = true
                    relatedTagsLayout.isVisible = true
                    relatedTagsLayout.setTagList(post.tags.map { it2 -> it2.tagName })
                }
            }

        }
    }
}