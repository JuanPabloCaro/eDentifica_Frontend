package com.app.edentifica.data.model

import com.google.android.gms.auth.api.credentials.IdentityProviders
import com.google.gson.annotations.SerializedName
import java.util.Arrays


enum class NetworkType {
    @SerializedName("FACEBOOK")
    FACEBOOK,
    @SerializedName("INSTAGRAM")
    INSTAGRAM,
    @SerializedName("TWITTER")
    TWITTER;

    companion object {
        fun fromString(string: String): NetworkType? {
            return values().find { it.name.equals(string, ignoreCase = true) }
        }
    }
}


