package com.app.edentifica.data.retrofit

import com.app.edentifica.data.model.Phone
import com.app.edentifica.data.model.SocialNetwork
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SocialNetworkService {
    @PUT("edentifica/social_networks/update")
    suspend fun updateSocialNetwork(@Body socialNetwork: SocialNetwork): Response<Boolean>

    @GET("edentifica/social_networks/get/{idprofile}")
    suspend fun listSocialNetworksUser(@Path("idprofile") idprofile: String): Response<Set<SocialNetwork>>

    @GET("edentifica/social_networks/get")
    suspend fun getSocialNetwork(@Query("id") id: String): Response<SocialNetwork>

    @DELETE("edentifica/social_networks/delete/{id}")
    suspend fun deleteSocialNetwork(@Path("id") id: String): Response<Boolean>
}