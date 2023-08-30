package com.tfm.musiccommunityapp.ui.community.discussions

import android.content.Context
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.DiscussionItemRowBinding
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.ui.extensions.bindingInflate
import com.tfm.musiccommunityapp.utils.formatDateToString
import com.tfm.musiccommunityapp.utils.getChipLabel

class DiscussionsAdapter(
    private val onDiscussionClicked: (DiscussionDomain) -> Unit
) : RecyclerView.Adapter<DiscussionsAdapter.DiscussionViewHolder>() {

    private val discussions: MutableList<DiscussionDomain> = mutableListOf()

    init {
        setHasStableIds(false)
    }

    override fun getItemCount(): Int = discussions.size

    override fun getItemId(position: Int): Long = discussions.getOrNull(position)?.id ?: 0

    fun setDiscussions(discussionList: List<DiscussionDomain>) {
        discussions.apply {
            clear()
            addAll(discussionList)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: DiscussionsAdapter.DiscussionViewHolder,
        position: Int
    ) {
        val context: Context = holder.itemView.context
        holder.bind(discussions[position], context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiscussionsAdapter.DiscussionViewHolder =
        DiscussionViewHolder(
            parent.bindingInflate(R.layout.discussion_item_row, false)
        ) { onDiscussionClicked(it) }

    inner class DiscussionViewHolder(
        private val binding: DiscussionItemRowBinding,
        callback: (DiscussionDomain) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { callback(discussions[bindingAdapterPosition]) }
        }

        fun bind (item: DiscussionDomain, context: Context) {
            binding.apply {
                relatedTagsLayout.setTagList(emptyList())
                tvRelatedTags.isVisible = false
                relatedTagsLayout.isVisible = false

                tvDiscussionChip.text = String.format(
                    context.getString(R.string.chip_post),
                    item.id,
                    getChipLabel(item.postType, context)
                )
                tvDiscussionTitle.text = item.title
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