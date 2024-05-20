package com.app.edentifica.data.model

import com.google.gson.annotations.SerializedName

data class Phone(
    @SerializedName("id") var id:String?,
    @SerializedName("phoneNumber") var phoneNumber:String,
    @SerializedName("isVerified") var isVerified:Boolean?,
    @SerializedName("idProfileUser") var idProfileUser:String?
)

