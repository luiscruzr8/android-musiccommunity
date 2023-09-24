package com.tfm.musiccommunityapp.data.datasource

import com.tfm.musiccommunityapp.api.UsersApi
import com.tfm.musiccommunityapp.api.extensions.DEFAULT_ERROR_400_MESSAGE
import com.tfm.musiccommunityapp.api.extensions.DEFAULT_ERROR_404_MESSAGE
import com.tfm.musiccommunityapp.api.extensions.DEFAULT_ERROR_500_MESSAGE
import com.tfm.musiccommunityapp.api.model.ErrorResponse
import com.tfm.musiccommunityapp.api.model.FollowerResponse
import com.tfm.musiccommunityapp.api.model.UserResponse
import com.tfm.musiccommunityapp.api.model.toDomain
import com.tfm.musiccommunityapp.data.datasource.remote.UserRemoteDatasourceImpl
import com.tfm.musiccommunityapp.data.model.FollowerResponseBuilder
import com.tfm.musiccommunityapp.data.model.UserResponseBuilder
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response
import java.net.HttpURLConnection
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class UserRemoteDatasourceImplTest {

    @MockK
    private val userApi = mockk<UsersApi>()

    private val sut by lazy { UserRemoteDatasourceImpl(userApi) }

    private val badRequestErrorString =  "{\"title\": \"$DEFAULT_ERROR_400_MESSAGE\", \"code\": \"${HttpURLConnection.HTTP_BAD_REQUEST}\", \"detail\": \"$DEFAULT_ERROR_400_MESSAGE\"}"
    private val notFoundErrorString =  "{\"title\": \"$DEFAULT_ERROR_404_MESSAGE\", \"code\": \"${HttpURLConnection.HTTP_NOT_FOUND}\", \"detail\": \"$DEFAULT_ERROR_404_MESSAGE\"}"
    private val internalErrorString =  "{\"title\": \"$DEFAULT_ERROR_500_MESSAGE\", \"code\": \"${HttpURLConnection.HTTP_INTERNAL_ERROR}\", \"detail\": \"$DEFAULT_ERROR_500_MESSAGE\"}"

    @Test
    fun `test that getAllUsers returns a list of users`() {
        val usersResponse = listOf(
            FollowerResponseBuilder.default,
            FollowerResponseBuilder.default.copy(id = 2, login = "User 2", bio = "Bio user 2"),
            FollowerResponseBuilder.default.copy(id = 3, login = "User 3", bio = "Bio user 3")
        )
        val usersDomain = usersResponse.map { it.toDomain() }
        coEvery { userApi.getAllUsers(null) } returns Response.success(usersResponse)

        val result = runBlocking { sut.getAllUsers(null) }

        coVerify { userApi.getAllUsers(null) }
        assertTrue(result.isRight())
        assertEquals(usersDomain, result.getOrNull())
    }

    @Test
    fun `test that getAllUsers returns an error`() {
        val errorResponse = Response.error<List<FollowerResponse>>(
            HttpURLConnection.HTTP_INTERNAL_ERROR,
            internalErrorString.toResponseBody()
        )
        val expected = ErrorResponse(
            code = HttpURLConnection.HTTP_INTERNAL_ERROR,
            message = DEFAULT_ERROR_500_MESSAGE
        ).toDomain()
        coEvery { userApi.getAllUsers(null) } returns errorResponse

        val result = runBlocking { sut.getAllUsers(null) }

        coVerify { userApi.getAllUsers(null) }
        assertTrue { result.isLeft() }
        assertEquals(expected.code, result.swap().getOrNull()?.code)
    }

    @Test
    fun `test that getUserInfo succeeds and returns information of the user`() {
        val userResponse = UserResponseBuilder.default
        val userDomain = userResponse.toDomain()
        coEvery { userApi.getUser(any()) } returns Response.success(userResponse)

        val result = runBlocking { sut.getUserInfo(userDomain.login) }

        coVerify { userApi.getUser(userDomain.login) }
        assertTrue(result.isRight())
        assertEquals(userDomain, result.getOrNull())
    }

    @Test
    fun `test that getUserInfo returns a 404 error`() {
        val errorResponse = Response.error<List<FollowerResponse>>(
            HttpURLConnection.HTTP_NOT_FOUND,
            notFoundErrorString.toResponseBody()
        )
        val expected = ErrorResponse(
            code = HttpURLConnection.HTTP_NOT_FOUND,
            message = DEFAULT_ERROR_404_MESSAGE
        ).toDomain()
        coEvery { userApi.getAllUsers(null) } returns errorResponse

        val result = runBlocking { sut.getAllUsers(null) }

        coVerify { userApi.getAllUsers(null) }
        assertTrue { result.isLeft() }
        assertEquals(expected.code, result.swap().getOrNull()?.code)
    }

    @Test
    fun `test that updateUserInfo succeeds and updates the user information`() {
        val userModified = UserResponseBuilder.default.copy(bio = "Bio modified")
        coEvery { userApi.updateUserInfo(any()) } returns Response.success(userModified)

        val result = runBlocking { sut.updateUserInfo(userModified.toDomain()) }

        coVerify { userApi.updateUserInfo(userModified) }
        assertTrue(result.isRight())
        assertEquals(userModified.toDomain(), result.getOrNull())
    }

    @Test
    fun `test that updateUserInfo returns a 400 error`() {
        val userModified = UserResponseBuilder.default.copy(bio = "Bio modified")
        val errorResponse = Response.error<UserResponse>(HttpURLConnection.HTTP_BAD_REQUEST, badRequestErrorString.toResponseBody())
        val expected = ErrorResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = DEFAULT_ERROR_400_MESSAGE).toDomain()

        coEvery { userApi.updateUserInfo(any()) } returns errorResponse

        val result = runBlocking { sut.updateUserInfo(userModified.toDomain()) }

        coVerify { userApi.updateUserInfo(userModified) }
        assertTrue(result.isLeft())
        assertEquals(expected.code, result.swap().getOrNull()?.code)
    }

    @Test
    fun `test that updateUserInfo returns a 404 error`() {
        val userModified = UserResponseBuilder.default.copy(bio = "Bio modified")
        val errorResponse = Response.error<UserResponse>(HttpURLConnection.HTTP_NOT_FOUND, notFoundErrorString.toResponseBody())
        val expected = ErrorResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = DEFAULT_ERROR_404_MESSAGE).toDomain()

        coEvery { userApi.updateUserInfo(any()) } returns errorResponse

        val result = runBlocking { sut.updateUserInfo(userModified.toDomain()) }

        coVerify { userApi.updateUserInfo(userModified) }
        assertTrue(result.isLeft())
        assertEquals(expected.code, result.swap().getOrNull()?.code)
    }

    @Test
    fun `test that updateUserInfo returns a 500 error`() {
        val userModified = UserResponseBuilder.default.copy(bio = "Bio modified")
        val errorResponse = Response.error<UserResponse>(HttpURLConnection.HTTP_INTERNAL_ERROR, internalErrorString.toResponseBody())
        val expected = ErrorResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = DEFAULT_ERROR_500_MESSAGE).toDomain()

        coEvery { userApi.updateUserInfo(any()) } returns errorResponse

        val result = runBlocking { sut.updateUserInfo(userModified.toDomain()) }

        coVerify { userApi.updateUserInfo(userModified) }
        assertTrue(result.isLeft())
        assertEquals(expected.code, result.swap().getOrNull()?.code)
    }

    @Test
    fun `test that getUserFollowers succeeds and get a list of followers for the current user`() {
        val user = null
        val followersResponse = listOf(
            FollowerResponseBuilder.default,
            FollowerResponseBuilder.default.copy(id = 2, login = "User 2", bio = "Bio user 2"),
            FollowerResponseBuilder.default.copy(id = 3, login = "User 3", bio = "Bio user 3")
        )
        val followersDomain = followersResponse.map { it.toDomain() }
        coEvery { userApi.getUserFollowers(any()) } returns Response.success(followersResponse)

        val result = runBlocking { sut.getUserFollowers(user) }

        coVerify { userApi.getUserFollowers(user) }
        assertTrue(result.isRight())
        assertEquals(followersDomain, result.getOrNull())
    }

    @Test
    fun `test that getUserFollowers succeeds and get a list of followers for other user`() {
        val user = "luis"
        val followersResponse = listOf<FollowerResponse>()
        val followersDomain = followersResponse.map { it.toDomain() }
        coEvery { userApi.getUserFollowers(any()) } returns Response.success(followersResponse)

        val result = runBlocking { sut.getUserFollowers(user) }

        coVerify { userApi.getUserFollowers(user) }
        assertTrue(result.isRight())
        assertEquals(followersDomain, result.getOrNull())
    }

    @Test
    fun `test that getUserFollowers returns a 500 error`() {
        val user = "test"
        val errorResponse = Response.error<List<FollowerResponse>>(HttpURLConnection.HTTP_INTERNAL_ERROR, internalErrorString.toResponseBody())
        val expected = ErrorResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = DEFAULT_ERROR_500_MESSAGE).toDomain()
        coEvery { userApi.getUserFollowers(any()) } returns errorResponse

        val result = runBlocking { sut.getUserFollowers(user) }

        coVerify { userApi.getUserFollowers(user) }
        assertTrue { result.isLeft() }
        assertEquals(expected.code, result.swap().getOrNull()?.code)
    }

    @Test
    fun `test that getUserFollowing succeeds and get a list of my following users`() {
        val followingResponse = listOf(
            FollowerResponseBuilder.default,
            FollowerResponseBuilder.default.copy(id = 2, login = "User 2", bio = "Bio user 2"),
            FollowerResponseBuilder.default.copy(id = 3, login = "User 3", bio = "Bio user 3")
        )
        val followingDomain = followingResponse.map { it.toDomain() }
        coEvery { userApi.getUserFollowing() } returns Response.success(followingResponse)

        val result = runBlocking { sut.getUserFollowing() }

        coVerify { userApi.getUserFollowing() }
        assertTrue(result.isRight())
        assertEquals(followingDomain, result.getOrNull())
    }

    @Test
    fun `test that getUserFollowing returns a 500 error`() {
        val errorResponse = Response.error<List<FollowerResponse>>(HttpURLConnection.HTTP_INTERNAL_ERROR, internalErrorString.toResponseBody())
        val expected = ErrorResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = DEFAULT_ERROR_500_MESSAGE).toDomain()
        coEvery { userApi.getUserFollowing() } returns errorResponse

        val result = runBlocking { sut.getUserFollowing() }

        coVerify { userApi.getUserFollowing() }
        assertTrue { result.isLeft() }
        assertEquals(expected.code, result.swap().getOrNull()?.code)
    }

    @Test
    fun `test that checkIfUserIsFollowerOf succeeds and returns if i'm following the user`() {
        val userIFollow = "luis"
        val userIDoNotFollow = "alonso"
        coEvery { userApi.isUserFollowerOf(userIFollow) } returns Response.success(200, true)
        coEvery { userApi.isUserFollowerOf(userIDoNotFollow) } returns Response.success(200, false)

        val result1 = runBlocking { sut.checkIfUserIsFollowerOf(userIFollow) }

        coVerify(exactly = 1) { userApi.isUserFollowerOf(userIFollow) }
        assertTrue { result1.isRight() }
        result1.getOrNull()?.let { assertTrue(it) }

        val result2 = runBlocking { sut.checkIfUserIsFollowerOf(userIDoNotFollow) }

        coVerify(exactly = 1) { userApi.isUserFollowerOf(userIDoNotFollow) }
        assertTrue { result2.isRight() }
        result2.getOrNull()?.let { assertFalse(it) }
    }

    @Test
    fun `test that checkIfUserIsFollowerOf returns a 500 error`() {
        val userIFollow = "luis"
        val errorResponse = Response.error<Boolean>(HttpURLConnection.HTTP_INTERNAL_ERROR, internalErrorString.toResponseBody())
        val expected = ErrorResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = DEFAULT_ERROR_500_MESSAGE).toDomain()
        coEvery { userApi.isUserFollowerOf(userIFollow) } returns errorResponse

        val result = runBlocking { sut.checkIfUserIsFollowerOf(userIFollow) }

        coVerify { userApi.isUserFollowerOf(userIFollow) }
        assertTrue { result.isLeft() }
        assertEquals(expected.code, result.swap().getOrNull()?.code)
    }

    @Test
    fun `test that followUser succeeds following an user`() {
        val userToFollow = "luis"
        coEvery { userApi.followUser(any()) } returns Response.success(Unit)

        val result = runBlocking { sut.followUser(userToFollow) }

        coVerify { userApi.followUser(userToFollow) }
        assertTrue(result.isRight())
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `test that followUser returns a 404 error`() {
        val userToFollow = "luis"
        val errorResponse = Response.error<Unit>(HttpURLConnection.HTTP_NOT_FOUND, notFoundErrorString.toResponseBody())
        val expected = ErrorResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = DEFAULT_ERROR_404_MESSAGE).toDomain()
        coEvery { userApi.followUser(any()) } returns errorResponse

        val result = runBlocking { sut.followUser(userToFollow) }

        coVerify { userApi.followUser(userToFollow) }
        assertTrue { result.isLeft() }
        assertEquals(expected.code, result.swap().getOrNull()?.code)
    }

    @Test
    fun `test that followUser returns a 500 error`() {
        val userToFollow = "luis"
        val errorResponse = Response.error<Unit>(HttpURLConnection.HTTP_INTERNAL_ERROR, internalErrorString.toResponseBody())
        val expected = ErrorResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = DEFAULT_ERROR_500_MESSAGE).toDomain()
        coEvery { userApi.followUser(any()) } returns errorResponse

        val result = runBlocking { sut.followUser(userToFollow) }

        coVerify { userApi.followUser(userToFollow) }
        assertTrue { result.isLeft() }
        assertEquals(expected.code, result.swap().getOrNull()?.code)
    }

    @Test
    fun `test that unfollowUser succeeds unfollowing an user`() {
        val userToUnfollow = "luis"
        coEvery { userApi.unfollowUser(any()) } returns Response.success(Unit)

        val result = runBlocking { sut.unfollowUser(userToUnfollow) }

        coVerify { userApi.unfollowUser(userToUnfollow) }
        assertTrue(result.isRight())
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `test that unfollowUser returns a 404 error`() {
        val userToUnfollow = "luis"
        val errorResponse = Response.error<Unit>(HttpURLConnection.HTTP_NOT_FOUND, notFoundErrorString.toResponseBody())
        val expected = ErrorResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = DEFAULT_ERROR_404_MESSAGE).toDomain()
        coEvery { userApi.unfollowUser(any()) } returns errorResponse

        val result = runBlocking { sut.unfollowUser(userToUnfollow) }

        coVerify { userApi.unfollowUser(userToUnfollow) }
        assertTrue { result.isLeft() }
        assertEquals(expected.code, result.swap().getOrNull()?.code)
    }

    @Test
    fun `test that unfollowUser returns a 500 error`() {
        val userToUnfollow = "luis"
        val errorResponse = Response.error<Unit>(HttpURLConnection.HTTP_INTERNAL_ERROR, internalErrorString.toResponseBody())
        val expected = ErrorResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = DEFAULT_ERROR_500_MESSAGE).toDomain()
        coEvery { userApi.unfollowUser(any()) } returns errorResponse

        val result = runBlocking { sut.unfollowUser(userToUnfollow) }

        coVerify { userApi.unfollowUser(userToUnfollow) }
        assertTrue { result.isLeft() }
        assertEquals(expected.code, result.swap().getOrNull()?.code)
    }

}
