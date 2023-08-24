package com.tfm.musiccommunityapp.data.datasource.remote

import arrow.core.Either
import com.tfm.musiccommunityapp.data.api.UsersApi
import com.tfm.musiccommunityapp.data.api.extensions.toErrorResponse
import com.tfm.musiccommunityapp.data.api.model.toDomain
import com.tfm.musiccommunityapp.data.api.model.toResponse
import com.tfm.musiccommunityapp.data.datasource.UserDatasource
import com.tfm.musiccommunityapp.data.extensions.eitherOf
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.domain.model.UserDomain

internal class UserRemoteDatasourceImpl(
    private val usersApi: UsersApi
) : UserDatasource {

    override suspend fun getAllUsers(username: String?): Either<DomainError, List<ShortUserDomain>> =
        eitherOf(
            response = usersApi.getAllUsers(username),
            mapper = { it -> it?.map { it.toDomain() } ?: emptyList() },
            errorMapper = { error -> error.toErrorResponse().toDomain() }
        )


    override suspend fun getUserInfo(username: String?): Either<DomainError, UserDomain?> =
        eitherOf(
            response = usersApi.getUser(username),
            mapper = { it?.toDomain() },
            errorMapper = { error -> error.toErrorResponse().toDomain() }
        )

    override suspend fun updateUserInfo(user: UserDomain): Either<DomainError, UserDomain?> = eitherOf(
        response = usersApi.updateUserInfo(user.toResponse()),
        mapper = { it?.toDomain() },
        errorMapper = { error -> error.toErrorResponse().toDomain() }
    )

    override suspend fun getUserFollowers(username: String?): Either<DomainError, List<ShortUserDomain>> = eitherOf(
        response = usersApi.getUserFollowers(username),
        mapper = { it -> it?.map { it.toDomain() } ?: emptyList() },
        errorMapper = { error -> error.toErrorResponse().toDomain() }
    )

    override suspend fun getUserFollowing(): Either<DomainError, List<ShortUserDomain>>  = eitherOf(
        response = usersApi.getUserFollowing(),
        mapper = { it -> it?.map { it.toDomain() } ?: emptyList() },
        errorMapper = { error -> error.toErrorResponse().toDomain() }
    )

    override suspend fun checkIfUserIsFollowerOf(user: String): Either<DomainError, Boolean?> = eitherOf(
        response = usersApi.isUserFollowerOf(user),
        mapper = { it },
        errorMapper = { error -> error.toErrorResponse().toDomain() }
    )

    override suspend fun followUser(user: String): Either<DomainError, Unit> = eitherOf(
        response = usersApi.followUser(user),
        mapper = { },
        errorMapper = { error -> error.toErrorResponse().toDomain() }
    )

    override suspend fun unfollowUser(user: String): Either<DomainError, Unit> = eitherOf(
        response = usersApi.unfollowUser(user),
        mapper = { },
        errorMapper = { error -> error.toErrorResponse().toDomain() }
    )
}