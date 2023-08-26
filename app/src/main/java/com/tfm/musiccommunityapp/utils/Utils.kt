package com.tfm.musiccommunityapp.utils

import android.content.Context
import android.graphics.Color
import com.tfm.musiccommunityapp.R
import kotlin.random.Random

fun getRandomColor(): Int {
    return Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
}

fun getChipLabel(text: String, context: Context): String =
    when (text) {
        "Anuncio" -> context.getString(R.string.advertisement)
        "Evento" -> context.getString(R.string.event)
        "Discusión" -> context.getString(R.string.discussion)
        else -> context.getString(R.string.opinion)
    }