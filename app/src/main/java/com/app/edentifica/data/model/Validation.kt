package com.app.edentifica.data.model

import com.google.gson.annotations.SerializedName

data class Validation(
    @SerializedName("id") var id:String,
    @SerializedName("challenge") var challenge:String,
//    @SerializedName("validated") var validated:String,
    @SerializedName("isValidated") var isValidated:Boolean,
)
