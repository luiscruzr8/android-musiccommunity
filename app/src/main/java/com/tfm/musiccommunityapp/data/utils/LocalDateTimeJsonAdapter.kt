package com.tfm.musiccommunityapp.data.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime

internal class LocalDateTimeJsonAdapter : TypeAdapter<LocalDateTime>() {

    private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun write(out: JsonWriter, value: LocalDateTime) {
        try {
            out.value(format.format(value))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun read(value: JsonReader?): LocalDateTime? {
        return if (value != null) {
            try {
                LocalDateTime.parse(value.nextString())
            } catch (e: ParseException) {
                e.printStackTrace()
                null
            }
        } else null
    }

}