package com.app.edentifica.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Profile(
    @SerializedName("id") var id:String?,
    @SerializedName("description") var description:String,
    @SerializedName("urlImageProfile")var urlImageProfile: String,
    @SerializedName("dateBirth")var dateBirth: LocalDate?
//    @SerializedName("isMultiuser") var isMultiuser:Boolean?,
//    @SerializedName("idUsers") var idUsers: Set<String>?,
//    @SerializedName("idAdmin") var idAdmin: String?
)
