package com.tfm.musiccommunityapp.data.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.data.datasource.UserDatasource
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.domain.model.UserDomain
import com.tfm.musiccommunityapp.domain.repository.UserProfileRepository

class UserProfileRepositoryImpl(
    private val userDatasource: UserDatasource
): UserProfileRepository {
    override suspend fun getUsers(): Either<DomainError, List<ShortUserDomain>> =
        userDatasource.getAllUsers()

    override suspend fun getUserInfo(username: String?): Either<DomainError, UserDomain?> =
        userDatasource.getUserInfo(username)

    override suspend fun updateUserInfo(user: UserDomain): Either<DomainError, UserDomain?> =
        userDatasource.updateUserInfo(user)

    override suspend fun getUserFollowers(username: String?): Either<DomainError, List<ShortUserDomain>> =
        userDatasource.getUserFollowers(username)

    override suspend fun getUserFollowing(): Either<DomainError, List<ShortUserDomain>> =
        userDatasource.getUserFollowing()

    override suspend fun checkIfUserIsFollowerOf(user: String): Either<DomainError, Boolean?> =
        userDatasource.checkIfUserIsFollowerOf(user)

    override suspend fun followUser(user: String): Either<DomainError, Unit> =
        userDatasource.followUser(user)

    override suspend fun unfollowUser(user: String): Either<DomainError, Unit> =
        userDatasource.unfollowUser(user)

}