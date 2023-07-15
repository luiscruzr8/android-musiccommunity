package com.tfm.musiccommunityapp.utils

import android.graphics.Color
import kotlin.random.Random

fun getRandomColor(): Int {
    return Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
}