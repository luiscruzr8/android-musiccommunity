package com.tfm.musiccommunityapp.data.extensions

import okhttp3.HttpUrl

val HttpUrl.server: String
    get() = host

val HttpUrl.isSignInRequest: Boolean
    get() = pathSegments.last() == "signin"

val HttpUrl.isSignUpRequest: Boolean
    get() = pathSegments.last() == "signup"

val HttpUrl.isSignOutRequest: Boolean
    get() = pathSegments.last() == "signout"