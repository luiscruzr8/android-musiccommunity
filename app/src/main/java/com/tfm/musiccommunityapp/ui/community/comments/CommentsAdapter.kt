package com.tfm.musiccommunityapp.ui.community.comments

import android.content.Context
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.CommentItemRowBinding
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.ui.extensions.bindingInflate
import com.tfm.musiccommunityapp.utils.formatDateTimeToString

class CommentsAdapter(
    private val onAddResponseClick: (CommentDomain) -> Unit,
    private val onDeleteCommentClicked: (CommentDomain) -> Unit
) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    private val comments: MutableList<CommentDomain> = mutableListOf()

    init {
        setHasStableIds(false)
    }

    fun setComments(commentsList: List<CommentDomain>) {
        comments.apply {
            clear()
            addAll(commentsList)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = comments.size

    override fun getItemId(position: Int): Long = comments.getOrNull(position)?.id ?: 0

    override fun onBindViewHolder(
        holder: CommentsAdapter.CommentViewHolder,
        position: Int
    ) {
        val context: Context = holder.itemView.context
        holder.bind(comments[position], context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentsAdapter.CommentViewHolder =
        CommentViewHolder(
            parent.bindingInflate(R.layout.comment_item_row, false)
        ) { onAddResponseClick(it) }

    inner class CommentViewHolder(
        private val binding: CommentItemRowBinding,
        callback: (CommentDomain) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivRespondButton.setOnClickListener { callback(comments[bindingAdapterPosition]) }
        }

        fun bind(item: CommentDomain, context: Context) {
            binding.apply {

                ivDeleteButton.setOnClickListener {
                    onDeleteCommentClicked(item)
                }
                chipUserComment.text = String.format(
                    context.getString(R.string.chip_comment_user), item.id, item.login
                )
                tvCommentedOn.text = String.format(
                    context.getString(R.string.commented_on),
                    item.commentedOn.formatDateTimeToString()
                )
                tvCommentText.text = item.text

                rvResponses.adapter = ResponsesAdapter(onDeleteCommentClicked)
                (rvResponses.adapter as ResponsesAdapter).setResponses(item.responses)

                clResponses.isVisible = item.responses.isNotEmpty()
            }
        }

    }

}