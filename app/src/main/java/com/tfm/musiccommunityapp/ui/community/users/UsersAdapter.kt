package com.tfm.musiccommunityapp.ui.community.users

import android.content.Context
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.FollowerItemRowBinding
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.ui.extensions.bindingInflate
import com.tfm.musiccommunityapp.utils.getRandomColor

class UsersAdapter (
    private val onItemClicked: (ShortUserDomain) -> Unit
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    private val users: MutableList<ShortUserDomain> = mutableListOf()

    init {
        setHasStableIds(false)
    }

    override fun getItemCount(): Int = users.size

    override fun getItemId(position: Int): Long  = users.getOrNull(position)?.id ?: 0

    fun setUsers(userList: List<ShortUserDomain>) {
        users.apply {
            clear()
            addAll(userList)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: UsersAdapter.UserViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        holder.bind(users[position], context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.UserViewHolder =
        UserViewHolder(
            parent.bindingInflate(R.layout.follower_item_row, false)
        ) { onItemClicked(it) }

    inner class UserViewHolder(
        private val binding: FollowerItemRowBinding,
        callback: (ShortUserDomain) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { callback(users[bindingAdapterPosition]) }
        }

        fun bind(item: ShortUserDomain, context: Context) {
            binding.apply {
                followerTagsLayout.setTagList(emptyList())
                tvFollowerUsername.text = item.login
                item.interests.let {interests ->
                    if (interests.isEmpty()) {
                        tvFollowerInterestsLabel.isVisible = false
                    } else {
                        tvFollowerInterestsLabel.isVisible = true
                        followerTagsLayout.setTagList(interests.map { it2 -> it2.tagName })
                    }

                }
                ivFollowerImage.setImageDrawable(
                    AvatarGenerator.AvatarBuilder(context)
                        .setLabel(item.login)
                        .setAvatarSize(64)
                        .setTextSize(22)
                        .toCircle()
                        .setBackgroundColor(getRandomColor())
                        .build()
                )
            }
        }
    }
}