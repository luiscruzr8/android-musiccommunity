package com.tfm.musiccommunityapp.ui.community.comments

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.ResponseItemRowBinding
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.ui.extensions.bindingInflate
import com.tfm.musiccommunityapp.utils.formatDateTimeToString

class ResponsesAdapter(
    private val onDeleteCommentClicked: (CommentDomain) -> Unit
) : RecyclerView.Adapter<ResponsesAdapter.ResponseViewHolder>() {

    private val responses: MutableList<CommentDomain> = mutableListOf()

    init {
        setHasStableIds(false)
    }

    fun setResponses(responsesList: List<CommentDomain>) {
        responses.apply {
            clear()
            addAll(responsesList)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = responses.size

    override fun getItemId(position: Int): Long = responses.getOrNull(position)?.id ?: 0

    override fun onBindViewHolder(
        holder: ResponsesAdapter.ResponseViewHolder,
        position: Int
    ) {
        val context: Context = holder.itemView.context
        holder.bind(responses[position], context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResponsesAdapter.ResponseViewHolder =
        ResponseViewHolder(
            parent.bindingInflate(R.layout.response_item_row, false)
        ) { onDeleteCommentClicked(it) }

    inner class ResponseViewHolder(
        private val binding: ResponseItemRowBinding,
        callback: (CommentDomain) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivDeleteButton.setOnClickListener { callback(responses[bindingAdapterPosition]) }
        }

        fun bind(item: CommentDomain, context: Context) {
            binding.apply {
                chipUserResponse.text = String.format(
                    context.getString(R.string.chip_comment_user), item.id, item.login
                )
                tvCommentedOn.text = String.format(
                    context.getString(R.string.responded_on),
                    item.commentedOn.formatDateTimeToString()
                )
                tvResponseText.text = item.text
            }
        }
    }
}