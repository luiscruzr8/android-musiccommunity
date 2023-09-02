package com.tfm.musiccommunityapp.domain.utils

import okhttp3.ResponseBody
import java.io.File

interface FileHelper {

    fun createFileFromResponseBody(responseBody: ResponseBody): File

}