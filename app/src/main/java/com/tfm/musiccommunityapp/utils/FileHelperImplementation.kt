package com.tfm.musiccommunityapp.utils

import android.content.Context
import com.tfm.musiccommunityapp.domain.utils.FileHelper
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

class FileHelperImplementation(private val context: Context): FileHelper {

    override fun createFileFromResponseBody(responseBody: ResponseBody): File {
        val file = File(context.cacheDir, "temp_file.pdf")
        val inputStream = responseBody.byteStream()
        val outputStream = FileOutputStream(file)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
}