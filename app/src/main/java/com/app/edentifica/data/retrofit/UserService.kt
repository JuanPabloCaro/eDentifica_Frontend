package com.app.edentifica.data.retrofit

import com.app.edentifica.data.model.User
import retrofit2.Response
import retrofit2.http.GET

interface UserService {
    @GET("edentifica/users/getall")
    suspend fun getAllUser() : Response<List<User>>
}