package com.tfm.musiccommunityapp.ui.extensions

import android.content.res.Resources

/**
 * Given dp, convert it to px
 */
val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()