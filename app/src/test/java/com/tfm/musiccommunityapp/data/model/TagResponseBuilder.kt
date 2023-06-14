package com.tfm.musiccommunityapp.data.model

import com.tfm.musiccommunityapp.data.api.model.TagResponse

object TagResponseBuilder {
    internal val default = TagResponse("tag1")

    internal fun defaultList() = listOf(default, default.copy(tagName = "tag2"))
}