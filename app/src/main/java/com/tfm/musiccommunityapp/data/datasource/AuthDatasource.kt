package com.tfm.musiccommunityapp.data.datasource

interface AuthDatasource {

    var encryptedUserName: String
    var encryptedPassword: String
    var accessToken: String
    val bearerToken: String

    fun storeCredentials(username: String, password: String) {
        this.encryptedUserName = username
        this.encryptedPassword = password
    }

    fun storeToken(accessToken: String) {
        this.accessToken = accessToken
    }

    fun clearData()
}