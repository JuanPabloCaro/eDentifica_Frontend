package com.app.edentifica.data.model

data class SocialNetwork(
    var id: String,
    var networkType: NetworkType,
    var profileName: String,
    var isVerified: Boolean
)
