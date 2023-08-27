package com.tfm.musiccommunityapp.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.formatDateTimeToString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    return this.format(formatter)
}

fun LocalDateTime.formatDateToString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")
    return this.format(formatter)
}

fun LocalDate.formatDateToString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    return this.format(formatter)
}