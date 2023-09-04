package com.tfm.musiccommunityapp.usecase.utils

import okhttp3.ResponseBody
import java.io.File

interface FileHelper {

    fun createFileFromResponseBody(responseBody: ResponseBody): File

}