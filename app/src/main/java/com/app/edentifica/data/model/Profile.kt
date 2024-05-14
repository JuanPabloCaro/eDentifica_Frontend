package com.app.edentifica.data.model

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("id") var id:String,
    @SerializedName("description") var description:String,
    @SerializedName("phones") var phones:Set<Phone>,
    @SerializedName("emails") var emails:Set<Email>,
    @SerializedName("socialNetworks") var socialNetworks: Set<SocialNetwork>,
    @SerializedName("isMultiuser") var isMultiuser:Boolean,
    @SerializedName("idUsers") var idUsers: Set<String>,
    @SerializedName("idAdmin") var idAdmin: String
)
