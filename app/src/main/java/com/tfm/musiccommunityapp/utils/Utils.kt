package com.tfm.musiccommunityapp.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.provider.OpenableColumns
import androidx.navigation.NavDirections
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.ui.community.recommendations.detail.RecommendationDetailFragmentDirections
import com.tfm.musiccommunityapp.ui.profile.posts.UserPostsFragmentDirections
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

fun getRandomColor(): Int {
    return Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
}

fun getChipLabel(text: String, context: Context): String =
    when (text) {
        "Announcement" -> context.getString(R.string.advertisement)
        "Event" -> context.getString(R.string.event)
        "Discussion" -> context.getString(R.string.discussion)
        "Opinion" -> context.getString(R.string.opinion)
        else -> text
    }

fun getChipColor(text: String, context: Context): ColorStateList =
    when (text) {
        "Announcement" -> ColorStateList.valueOf(context.getColor(R.color.advertisementColor))
        "Event" -> ColorStateList.valueOf(context.getColor(R.color.eventColor))
        "Discussion" -> ColorStateList.valueOf(context.getColor(R.color.discussionColor))
        else -> ColorStateList.valueOf(context.getColor(R.color.opinionColor))
    }

fun uriToFile(uri: Uri, context: Context): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    cursor?.moveToFirst()
    val name = cursor?.getString(nameIndex ?: 0)
    cursor?.close()
    val dir = context.cacheDir
    val file = File(dir, name)
    val outputStream = FileOutputStream(file)
    inputStream?.copyTo(outputStream)
    inputStream?.close()
    outputStream.close()
    return file
}

fun getPostType(posType: Int): String =
    when (posType) {
        R.string.advertisement -> "Announcement"
        R.string.event -> "Event"
        R.string.discussion -> "Discussion"
        R.string.opinion -> "Opinion"
        else -> ""
    }

fun navigateFromUserPostsOnPostType(
    posType: String,
    postId: Long,
    navigation: (NavDirections) -> Unit
) =
    when (posType) {
        "Announcement" ->
            navigation(
                UserPostsFragmentDirections.actionUserPostsFragmentToAdvertisementDetailFragment(
                    postId
                )
            )

        "Event" ->
            navigation(
                UserPostsFragmentDirections.actionUserPostsFragmentToEventDetailFragment(
                    postId
                )
            )

        "Discussion" ->
            navigation(
                UserPostsFragmentDirections.actionUserPostsFragmentToDiscussionDetailFragment(
                    postId
                )
            )

        "Opinion" ->
            navigation(
                UserPostsFragmentDirections.actionUserPostsFragmentToOpinionDetailFragment(
                    postId
                )
            )

        else -> {}
    }

fun navigateFromRecommendationOnPostType(
    posType: String,
    postId: Long,
    navigation: (NavDirections) -> Unit
) =
    when (posType) {
        "Announcement" ->
            navigation(
                RecommendationDetailFragmentDirections.actionRecommendationDetailFragmentToAdvertisementDetailFragment(
                    postId
                )
            )

        "Event" ->
            navigation(
                RecommendationDetailFragmentDirections.actionRecommendationDetailFragmentToEventDetailFragment(
                    postId
                )
            )

        "Discussion" ->
            navigation(
                RecommendationDetailFragmentDirections.actionRecommendationDetailFragmentToDiscussionDetailFragment(
                    postId
                )
            )

        "Opinion" ->
            navigation(
                RecommendationDetailFragmentDirections.actionRecommendationDetailFragmentToOpinionDetailFragment(
                    postId
                )
            )

        else -> {}
    }