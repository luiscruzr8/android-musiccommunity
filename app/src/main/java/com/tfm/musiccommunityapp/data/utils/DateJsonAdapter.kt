package com.tfm.musiccommunityapp.data.utils

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

internal class DateJsonAdapter: TypeAdapter<Date>() {

    private val format = SimpleDateFormat("yyyy-MM-dd")

    override fun write(out: JsonWriter, value: Date) {
        try {
            out.value(format.format(value))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun read(value: JsonReader?): Date? {
        return if (value != null) {
            try {
                format.parse(value.nextString())
            } catch (e: ParseException) {
                e.printStackTrace()
                null
            }
        } else null
    }

}