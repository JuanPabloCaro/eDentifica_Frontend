package com.app.edentifica.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApi {
    private val  BASE_URL = "http://192.168.1.38:8080/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userService : UserService by lazy { retrofit.create(UserService::class.java) }
    val phoneService : PhoneService by lazy { retrofit.create(PhoneService::class.java) }
}