package com.tfm.musiccommunityapp.ui.community.recommendations

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.RecommendationItemRowBinding
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.ui.extensions.bindingInflate
import com.tfm.musiccommunityapp.utils.formatDateToString
import com.tfm.musiccommunityapp.utils.getChipColor
import com.tfm.musiccommunityapp.utils.getChipLabel

class RecommendationsAdapter (
    private val onRecommendationClicked: (RecommendationDomain) -> Unit
) : RecyclerView.Adapter<RecommendationsAdapter.RecommendationViewHolder>() {

    private val recommendations: MutableList<RecommendationDomain> = mutableListOf()

    init {
        setHasStableIds(false)
    }

    override fun getItemCount(): Int = recommendations.size

    override fun getItemId(position: Int): Long = recommendations.getOrNull(position)?.id ?: 0

    fun setRecommendations(recommendationList: List<RecommendationDomain>) {
        recommendations.apply {
            clear()
            addAll(recommendationList)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: RecommendationsAdapter.RecommendationViewHolder,
        position: Int
    ) {
        val context: Context = holder.itemView.context
        holder.bind(recommendations[position], context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendationsAdapter.RecommendationViewHolder =
        RecommendationViewHolder(
            parent.bindingInflate(R.layout.recommendation_item_row, false)
        ) { onRecommendationClicked(it) }

    inner class RecommendationViewHolder(
        private val binding: RecommendationItemRowBinding,
        callback: (RecommendationDomain) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { callback(recommendations[bindingAdapterPosition]) }
        }

        fun bind(item: RecommendationDomain, context: Context) {
            binding.apply {
                tvRecommendationTitle.text = item.recommendationTitle
                tvRecCreationDate.text = item.createdOn.formatDateToString()
                tvRecommendationRating.text = String.format(context.getString(R.string.chip_rating), item.rating)
                tvPostTitle.text = item.post.title
                tvCreationDate.text = item.post.createdOn.formatDateToString()
                with(item.post.postType) {
                    tvPostChip.text = String.format(
                        context.getString(R.string.chip_post),
                        item.postId,
                        getChipLabel(this, context)
                    )
                    tvPostChip.chipBackgroundColor = getChipColor(this, context)
                }

            }
        }
    }

}