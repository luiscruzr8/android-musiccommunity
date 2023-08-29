package com.tfm.musiccommunityapp.data.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal class LocalDateTimeJsonAdapter : TypeAdapter<LocalDateTime>() {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: LocalDateTime?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(formatter.format(value))
        }
    }

    @Throws(IOException::class)
    override fun read(input: JsonReader): LocalDateTime? {
        return when (input.peek()) {
            JsonToken.NULL -> {
                input.nextNull()
                null
            }
            else -> try {
                LocalDateTime.parse(input.nextString(), formatter)
        } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

}