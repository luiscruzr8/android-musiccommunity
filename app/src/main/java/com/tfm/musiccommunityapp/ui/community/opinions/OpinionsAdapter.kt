package com.tfm.musiccommunityapp.ui.community.opinions

import android.content.Context
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.OpinionItemRowBinding
import com.tfm.musiccommunityapp.domain.model.OpinionDomain
import com.tfm.musiccommunityapp.ui.extensions.bindingInflate
import com.tfm.musiccommunityapp.utils.formatDateToString
import com.tfm.musiccommunityapp.utils.getChipLabel

class OpinionsAdapter(
    private val onOpinionClicked: (OpinionDomain) -> Unit
) : RecyclerView.Adapter<OpinionsAdapter.OpinionViewHolder>() {

    private val opinions: MutableList<OpinionDomain> = mutableListOf()

    init {
        setHasStableIds(false)
    }

    override fun getItemCount(): Int = opinions.size

    override fun getItemId(position: Int): Long = opinions.getOrNull(position)?.id ?: 0

    fun setOpinions(opinionList: List<OpinionDomain>) {
        opinions.apply {
            clear()
            addAll(opinionList)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: OpinionsAdapter.OpinionViewHolder,
        position: Int
    ) {
        val context: Context = holder.itemView.context
        holder.bind(opinions[position], context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OpinionsAdapter.OpinionViewHolder =
        OpinionViewHolder(
            parent.bindingInflate(R.layout.opinion_item_row, false)
        ) { onOpinionClicked(it) }

    inner class OpinionViewHolder(
        private val binding: OpinionItemRowBinding,
        callback: (OpinionDomain) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { callback(opinions[bindingAdapterPosition]) }
        }

        fun bind(item: OpinionDomain, context: Context) {
            binding.apply {
                relatedTagsLayout.setTagList(emptyList())
                tvRelatedTags.isVisible = false
                relatedTagsLayout.isVisible = false

                tvOpinionChip.text = String.format(
                    context.getString(R.string.chip_post),
                    item.id,
                    getChipLabel(item.postType, context)
                )
                tvOpinionTitle.text = item.title
                tvCreationDate.text = item.createdOn.formatDateToString()
                item.tags.let { tags ->
                    if (tags.isNotEmpty()) {
                        tvRelatedTags.isVisible = true
                        relatedTagsLayout.isVisible = true
                        relatedTagsLayout.setTagList(tags.map { it.tagName })
                    }
                }
            }
        }
    }
}