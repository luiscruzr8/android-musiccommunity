package com.tfm.musiccommunityapp.ui.dialogs.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.RatingDialogBinding

class RatingDialog(
    private val recommendationId: Long,
    private val onSaveClicked: (Long,Int) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: RatingDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RatingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            ratingTextView.text = getString(R.string.community_recommendations_rating_value, 0)

            submitRateButton.setOnClickListener {
                onSaveClicked(recommendationId, binding.recommendationRatingBar.rating.toInt())
                dismiss()
            }

            dismissButton.setOnClickListener {
                dismiss()
            }

            recommendationRatingBar.setOnRatingBarChangeListener { _, rating, _ ->
                ratingTextView.text = getString(R.string.community_recommendations_rating_value, rating.toInt())
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}