package com.app.edentifica.data.retrofit

import com.app.edentifica.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @GET("edentifica/users/get_by_email/{email}")
    suspend fun getByEmail(@Path("email")email: String): Response<User>

    @POST("edentifica/users/validation_one_call")
    suspend fun toDoCall(@Body user: User): Response<Boolean>

}