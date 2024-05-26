package com.app.edentifica.data.retrofit

import com.app.edentifica.data.model.Email
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface EmailService {
    @PUT("edentifica/emails/update")
    suspend fun updateEmail(@Body email: Email): Response<Boolean>

    @GET("edentifica/emails/get/{idprofile}")
    suspend fun listEmailsUser(@Path("idprofile") idprofile: String): Response<Set<Email>>
}