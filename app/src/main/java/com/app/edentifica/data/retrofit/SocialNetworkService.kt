package com.app.edentifica.data.retrofit

import com.app.edentifica.data.model.SocialNetwork
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface SocialNetworkService {
    @PUT("edentifica/social_networks/update")
    suspend fun updateSocialNetwork(@Body socialNetwork: SocialNetwork): Response<Boolean>

    @GET("edentifica/social_networks/get/{idprofile}")
    suspend fun listSocialNetworksUser(@Path("idprofile") idprofile: String): Response<Set<SocialNetwork>>
}