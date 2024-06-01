package com.app.edentifica.data.retrofit

import com.app.edentifica.data.model.Email
import com.app.edentifica.data.model.Phone
import com.app.edentifica.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PhoneService {
    @PUT("edentifica/phones/update")
    suspend fun updatePhone(@Body phone: Phone): Response<Boolean>

    @GET("edentifica/phones/get/{idprofile}")
    suspend fun listPhonesUser(@Path("idprofile") idprofile: String): Response<Set<Phone>>

    @GET("edentifica/phones/get")
    suspend fun getPhone(@Query("id") id: String): Response<Phone>

    @DELETE("edentifica/phones/delete/{id}")
    suspend fun deletePhone(@Path("id") id: String): Response<Boolean>
}