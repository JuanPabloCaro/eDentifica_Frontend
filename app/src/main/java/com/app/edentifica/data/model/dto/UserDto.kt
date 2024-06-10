package com.app.edentifica.data.model.dto

import com.app.edentifica.data.model.Email
import com.app.edentifica.data.model.Phone
import com.app.edentifica.data.model.Validation
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") var id:String?,
    @SerializedName("edentificador") var edentificador:String?,
    @SerializedName("name")var name:String,
    @SerializedName("lastName")var lastName:String,
    @SerializedName("validations")var validations: List<Validation>?
)