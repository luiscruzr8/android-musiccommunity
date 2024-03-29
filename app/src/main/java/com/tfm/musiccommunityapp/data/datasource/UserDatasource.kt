package com.tfm.musiccommunityapp.data.datasource

import arrow.core.Either
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.domain.model.UserDomain

interface UserDatasource {

    //region User

    suspend fun getAllUsers(username: String?): Either<DomainError, List<ShortUserDomain>>

    suspend fun getUserInfo(username: String?): Either<DomainError, UserDomain?>

    suspend fun updateUserInfo(user: UserDomain): Either<DomainError, UserDomain?>

    //endregion

    //region Followers

    suspend fun getUserFollowers(username: String?): Either<DomainError, List<ShortUserDomain>>

    suspend fun getUserFollowing(): Either<DomainError, List<ShortUserDomain>>

    suspend fun checkIfUserIsFollowerOf(user: String): Either<DomainError, Boolean?>

    suspend fun followUser(user: String): Either<DomainError, Unit>

    suspend fun unfollowUser(user: String): Either<DomainError, Unit>

    //endregion

}