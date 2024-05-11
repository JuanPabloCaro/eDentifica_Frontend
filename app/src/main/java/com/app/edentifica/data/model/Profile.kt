package com.app.edentifica.data.model

import java.time.LocalDate

data class Profile(
    var id:String,
    var description:String,
    var phones:Set<Phone>,
    var emails:Set<Email>,
    var socialNetworks: Set<SocialNetwork>,
    var isMultiuser:Boolean,
    var idUsers: Set<String>,
    var idAdmin: String
)
