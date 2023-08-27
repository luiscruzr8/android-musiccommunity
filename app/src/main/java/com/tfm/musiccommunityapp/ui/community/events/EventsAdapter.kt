package com.tfm.musiccommunityapp.ui.community.events

import android.content.Context
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.EventItemRowBinding
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.ui.extensions.bindingInflate
import com.tfm.musiccommunityapp.utils.formatDateTimeToString
import com.tfm.musiccommunityapp.utils.getChipLabel

class EventsAdapter(
    private val onItemClicked: (EventDomain) -> Unit
) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    private val events: MutableList<EventDomain> = mutableListOf()

    init {
        setHasStableIds(false)
    }

    override fun getItemCount(): Int = events.size

    override fun getItemId(position: Int): Long  = events.getOrNull(position)?.id ?: 0

    fun setEvents(eventList: List<EventDomain>) {
        events.apply {
            clear()
            addAll(eventList)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: EventsAdapter.EventViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        holder.bind(events[position], context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsAdapter.EventViewHolder =
        EventViewHolder(
            parent.bindingInflate(R.layout.event_item_row, false)
        ) { onItemClicked(it) }

    inner class EventViewHolder(
        private val binding: EventItemRowBinding,
        callback: (EventDomain) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { callback(events[bindingAdapterPosition]) }
        }

        fun bind(item: EventDomain, context: Context) {
            binding.apply {
                relatedTagsLayout.setTagList(emptyList())
                tvRelatedTags.isVisible = false
                relatedTagsLayout.isVisible = false

                tvEventChip.text = String.format(
                    context.getString(R.string.chip_post),
                    item.id,
                    getChipLabel(item.postType, context)
                )
                tvEventTitle.text = item.title
                tvEventLocation.text = item.cityName
                tvCreationDate.text = item.createdOn.formatDateTimeToString()
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