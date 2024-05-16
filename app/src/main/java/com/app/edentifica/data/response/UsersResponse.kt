package com.app.edentifica.data.response

import com.app.edentifica.data.model.User

data class UsersResponse(
    var status: String,
    var message: String,
    var data: User
)
