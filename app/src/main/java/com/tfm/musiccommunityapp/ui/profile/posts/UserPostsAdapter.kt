package com.tfm.musiccommunityapp.ui.profile.posts

import android.content.Context
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.UserPostItemBinding
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.ui.extensions.bindingInflate
import com.tfm.musiccommunityapp.ui.utils.formatDateToString
import com.tfm.musiccommunityapp.ui.utils.getChipColor
import com.tfm.musiccommunityapp.ui.utils.getChipLabel

class UserPostsAdapter(
    private val onPostClicked: (GenericPostDomain) -> Unit
) : RecyclerView.Adapter<UserPostsAdapter.UserPostViewHolder>(){

    private val posts: MutableList<GenericPostDomain> = mutableListOf()

    fun setPosts(postList: List<GenericPostDomain>) {
        posts.apply {
            clear()
            addAll(postList)
        }
        notifyDataSetChanged()
    }

    init {
        setHasStableIds(false)
    }

    override fun getItemCount(): Int = posts.size

    override fun getItemId(position: Int): Long = posts.getOrNull(position)?.id ?: 0

    override fun onBindViewHolder(
        holder: UserPostsAdapter.UserPostViewHolder,
        position: Int
    ) {
        val context: Context = holder.itemView.context
        holder.bind(posts[position], context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserPostsAdapter.UserPostViewHolder =
        UserPostViewHolder(
            parent.bindingInflate(R.layout.user_post_item, false)
        ) { onPostClicked(it) }

    inner class UserPostViewHolder(
        private val binding: UserPostItemBinding,
        callback: (GenericPostDomain) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { callback(posts[bindingAdapterPosition]) }
        }

        fun bind (item: GenericPostDomain, context: Context) {
            binding.apply {
                relatedTagsLayout.setTagList(emptyList())
                tvRelatedTags.isVisible = false
                relatedTagsLayout.isVisible = false

                tvPostsChip.chipBackgroundColor = getChipColor(item.postType, context)
                tvPostsChip.text = String.format(
                    context.getString(R.string.chip_post),
                    item.id,
                    getChipLabel(item.postType, context)
                )
                tvPostTitle.text = item.title
                tvPostCreationDate.text = item.createdOn.formatDateToString()
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