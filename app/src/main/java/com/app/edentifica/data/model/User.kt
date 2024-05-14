package com.app.edentifica.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class User(
    @SerializedName("id") var id:String,
    @SerializedName("name")var name:String,
    @SerializedName("lastName")var lastName:String,
    @SerializedName("dateBirth")var dateBirth: LocalDate,
    @SerializedName("phone")var phone:Phone,
    @SerializedName("email")var email:Email,
    @SerializedName("profile")var profile:Profile,
    @SerializedName("idProfiles")var idProfiles: Set<String>,
    @SerializedName("validations")var validations: List<Validation>
)
