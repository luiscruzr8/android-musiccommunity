package com.tfm.musiccommunityapp.data.repository

import com.tfm.musiccommunityapp.data.datasource.GenericPostDatasource
import com.tfm.musiccommunityapp.domain.repository.CommonPostRepository

class CommonPostRepositoryImpl(
    private val commonDatasource: GenericPostDatasource
): CommonPostRepository {
}