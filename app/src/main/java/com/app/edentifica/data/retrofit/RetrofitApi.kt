package com.app.edentifica.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApi {
    private val  BASE_URL = "https://rnql1vx4-8080.uks1.devtunnels.ms/" //IMPORTANTE CAMBIAR DEPENDIENDO DEL WIFI O RED
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userService : UserService by lazy { retrofit.create(UserService::class.java) }
    val phoneService : PhoneService by lazy { retrofit.create(PhoneService::class.java) }
    val emailService : EmailService by lazy { retrofit.create(EmailService::class.java) }
    val socialNetworkService : SocialNetworkService by lazy { retrofit.create(SocialNetworkService::class.java) }
    val profileService : ProfileService by lazy { retrofit.create(ProfileService::class.java) }
}