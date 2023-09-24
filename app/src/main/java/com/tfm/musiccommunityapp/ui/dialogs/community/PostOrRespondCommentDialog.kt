package com.tfm.musiccommunityapp.ui.dialogs.community

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.PostCommentDialogBinding
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import java.time.LocalDateTime

class PostOrRespondCommentDialog(
    private val onSendClicked: (CommentDomain) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: PostCommentDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PostCommentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            submitComment.setOnClickListener {
                if(!validate()) {
                    return@setOnClickListener
                }
                onSendClicked(extractValues()).run {
                    dismiss()
                }
            }
            dismissButton.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validate(): Boolean {
        var isValid = true
        binding.apply {
            val comment = commentTextEditText.text.toString().trim()

            if (comment.isEmpty()) {
                commentText.error = getString(R.string.error_empty_field)
                isValid = false
            } else commentText.error = null
        }
        return isValid
    }

    private fun extractValues(): CommentDomain {
        binding.apply {
            return CommentDomain(
                id = 0L,
                login = "",
                text = commentTextEditText.text.toString().trim(),
                commentedOn = LocalDateTime.now(),
                responses = emptyList()
            )
        }
    }
}