package com.tfm.musiccommunityapp.ui.scores

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.ItemScoreCardBinding
import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.ui.extensions.bindingInflate
import com.tfm.musiccommunityapp.utils.formatDateToString

class ScoresAdapter (
    private val onItemClicked: (ScoreDomain) -> Unit
) : RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder>() {

    private val scores: MutableList<ScoreDomain> = mutableListOf()

    init {
        setHasStableIds(false)
    }

    fun setScores(scoreList: List<ScoreDomain>) {
        scores.apply {
            clear()
            addAll(scoreList)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = scores.size

    override fun getItemId(position: Int): Long  = scores.getOrNull(position)?.id ?: 0

    override fun onBindViewHolder(holder: ScoresAdapter.ScoreViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        holder.bind(scores[position], context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScoresAdapter.ScoreViewHolder =
        ScoreViewHolder(
            parent.bindingInflate(R.layout.item_score_card, false)
        ) { onItemClicked(it) }

    inner class ScoreViewHolder(
        private val binding: ItemScoreCardBinding,
        callback: (ScoreDomain) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { callback(scores[bindingAdapterPosition]) }
        }

        fun bind(item: ScoreDomain, context: Context) {
            binding.apply {
                tvScoreChip.text = String.format(
                    context.getString(R.string.chip_score),
                    item.id
                )
                tvScoreTitle.text = item.name
                tvCreatedByOn.text = String.format(
                    context.getString(R.string.scores_screen_created_by_on),
                    item.login,
                    item.uploadedOn.formatDateToString()
                )
            }
        }
    }
}