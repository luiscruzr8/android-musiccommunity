package com.tfm.musiccommunityapp.domain.interactor.model

import com.tfm.musiccommunityapp.domain.model.TagDomain

object TagDomainBuilder {
    internal val default = TagDomain("tag1")

    internal fun defaultList() = listOf(default, default.copy(tagName = "tag2"))
}