package com.tfm.musiccommunityapp.ui.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.formatDateTimeToString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")
    return this.format(formatter)
}

fun LocalDateTime.formatDateToString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    return this.format(formatter)
}

fun LocalDateTime.formatTimeToString(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return this.format(formatter)
}

fun LocalDate.formatDateToString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    return this.format(formatter)
}

fun LocalTime.formatTimeToString(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return this.format(formatter)
}

fun String.localDateOf(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    return LocalDate.parse(this, formatter)
}

fun String.localTimeOf(): LocalTime {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return LocalTime.parse(this, formatter)
}

fun String.localDateTimeOf(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")
    return LocalDateTime.parse(this, formatter)
}