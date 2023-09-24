package com.tfm.musiccommunityapp.api.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class LocalDateJsonAdapter : TypeAdapter<LocalDate>() {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: LocalDate?) {
        out.value(value?.format(formatter))
    }

    @Throws(IOException::class)
    override fun read(input: JsonReader): LocalDate? {
        return LocalDate.parse(input.nextString(), formatter)
    }

}