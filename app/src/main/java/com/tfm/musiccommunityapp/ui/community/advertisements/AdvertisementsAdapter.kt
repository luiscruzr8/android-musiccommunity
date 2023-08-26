package com.tfm.musiccommunityapp.ui.community.advertisements

import android.content.Context
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.AdvertisementItemRowBinding
import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.ui.extensions.bindingInflate
import com.tfm.musiccommunityapp.utils.formatDateTimeToString
import com.tfm.musiccommunityapp.utils.getChipLabel

class AdvertisementsAdapter(
    private val onItemClicked: (AdvertisementDomain) -> Unit
) : RecyclerView.Adapter<AdvertisementsAdapter.AdvertisementViewHolder>() {

    private val advertisements: MutableList<AdvertisementDomain> = mutableListOf()

    init {
        setHasStableIds(false)
    }

    override fun getItemCount(): Int = advertisements.size

    override fun getItemId(position: Int): Long = advertisements.getOrNull(position)?.id ?: 0

    fun setAdvertisements(advertisementList: List<AdvertisementDomain>) {
        advertisements.apply {
            clear()
            addAll(advertisementList)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: AdvertisementsAdapter.AdvertisementViewHolder,
        position: Int
    ) {
        val context: Context = holder.itemView.context
        holder.bind(advertisements[position], context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdvertisementsAdapter.AdvertisementViewHolder =
        AdvertisementViewHolder(
            parent.bindingInflate(R.layout.advertisement_item_row, false)
        ) { onItemClicked(it) }


    inner class AdvertisementViewHolder(
        private val binding: AdvertisementItemRowBinding,
        callback: (AdvertisementDomain) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { callback(advertisements[bindingAdapterPosition]) }
        }

        fun bind(item: AdvertisementDomain, context: Context) {
            binding.apply {
                relatedTagsLayout.setTagList(emptyList())
                tvAdvertisementChip.text = String.format(
                    context.getString(R.string.chip_post),
                    item.id,
                    getChipLabel(item.postType, context)
                )
                tvAdvertisementTitle.text = item.title
                tvAdvertisementLocation.text = item.cityName
                tvCreationDate.text = item.createdOn.formatDateTimeToString()
                item.tags.let { tags ->
                    if (tags.isEmpty()) {
                        relatedTagsLayout.isVisible = false
                    } else {
                        relatedTagsLayout.isVisible = true
                        relatedTagsLayout.setTagList(tags.map { it.tagName })
                    }
                }
            }
        }
    }
}