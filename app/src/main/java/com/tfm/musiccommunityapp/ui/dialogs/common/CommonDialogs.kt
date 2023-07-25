package com.tfm.musiccommunityapp.ui.dialogs.common

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun alertDialogOneOption(
    context: Context,
    title: String,
    icon: Drawable?,
    message: String,
    positiveLabel: String,
    positiveCallback: (() -> Unit)?
) : AlertDialog =
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setIcon(icon)
        .setMessage(message)
        .setPositiveButton(positiveLabel) { dialog, which -> positiveCallback?.invoke() }
        .show()

fun alertDialogTwoOptions(
    context: Context,
    title: String,
    icon: Drawable?,
    message: String,
    positiveLabel: String,
    positiveCallback: (() -> Unit)?,
    negativeLabel: String,
    negativeCallback: (() -> Unit)?,
) : AlertDialog =
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setIcon(icon)
        .setMessage(message)
        .setPositiveButton(positiveLabel) { dialog, which -> positiveCallback?.invoke() }
        .setNegativeButton(negativeLabel) { dialog, which -> negativeCallback?.invoke() }
        .show()

fun alertDialogThreeOptions(
    context: Context,
    title: String,
    icon: Drawable?,
    message: String,
    positiveLabel: String,
    positiveCallback: (() -> Unit)?,
    negativeLabel: String,
    negativeCallback: (() -> Unit)?,
    neutralLabel: String,
    neutralCallback: (() -> Unit)?,
) : AlertDialog =
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setIcon(icon)
        .setMessage(message)
        .setPositiveButton(positiveLabel) { dialog, which -> positiveCallback?.invoke() }
        .setNegativeButton(negativeLabel) { dialog, which -> negativeCallback?.invoke() }
        .setNegativeButton(neutralLabel) { dialog, which -> neutralCallback?.invoke() }
        .show()