package com.app.edentifica.data.retrofit

import com.app.edentifica.data.response.UsersResponse
import retrofit2.Response
import retrofit2.http.GET

interface WebService {
    @GET("/edentifica/users/getall")
    suspend fun getAllUsers(): Response<UsersResponse>
}