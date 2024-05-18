package com.app.edentifica.data.model

import com.google.gson.annotations.SerializedName

data class SocialNetwork(
    @SerializedName("id") var id: String?,
    @SerializedName("networkType") var networkType: NetworkType,
    @SerializedName("profileName") var profileName: String,
    @SerializedName("isVerified") var isVerified: Boolean?,
    @SerializedName("idProfileUser") var idProfileUser:String?
)
