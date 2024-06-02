package com.app.edentifica.data.retrofit

import com.app.edentifica.data.retrofit.adapters.LocalDateAdapter
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object RetrofitApi {
    private val  BASE_URL = "https://rnql1vx4-8080.uks1.devtunnels.ms/" //IMPORTANTE CAMBIAR DEPENDIENDO DEL WIFI O RED

    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()




    val userService : UserService by lazy { retrofit.create(UserService::class.java) }
    val phoneService : PhoneService by lazy { retrofit.create(PhoneService::class.java) }
    val emailService : EmailService by lazy { retrofit.create(EmailService::class.java) }
    val socialNetworkService : SocialNetworkService by lazy { retrofit.create(SocialNetworkService::class.java) }
    val profileService : ProfileService by lazy { retrofit.create(ProfileService::class.java) }


}
