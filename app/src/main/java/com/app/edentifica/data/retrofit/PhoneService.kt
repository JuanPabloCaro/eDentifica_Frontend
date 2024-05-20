package com.app.edentifica.data.retrofit

import com.app.edentifica.data.model.Phone
import com.app.edentifica.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface PhoneService {
    @PUT("edentifica/phones/update")
    suspend fun updatePhone(@Body phone: Phone): Response<Boolean>
}