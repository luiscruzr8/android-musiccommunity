package com.tfm.musiccommunityapp.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import com.tfm.musiccommunityapp.R
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