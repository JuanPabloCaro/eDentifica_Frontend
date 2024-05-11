package com.app.edentifica.data.model

import java.time.LocalDate

data class User(
    var id:String,
    var name:String,
    var lastName:String,
    var dateBirth: LocalDate,
    var phone:Phone,
    var email:Email,
    var profile:Profile,
    var idProfiles: Set<String>,
    var validations: List<Validation>
)
