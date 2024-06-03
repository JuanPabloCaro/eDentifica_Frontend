package com.app.edentifica.data.retrofit

import androidx.lifecycle.ViewModel
import com.app.edentifica.data.model.Email
import com.app.edentifica.data.model.Phone
import com.app.edentifica.data.model.Profile
import com.app.edentifica.data.model.SocialNetwork
import com.app.edentifica.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileService {
    @GET("edentifica/profiles/get")
    suspend fun getProfileById(@Query("id") id: String): Response<Profile>

    @PUT("edentifica/profiles/update")
    suspend fun updateProfile(@Body profile: Profile): Response<Boolean>

    @POST("edentifica/profiles/insert_email/{profileId}")
    suspend fun insertEmail(@Path("profileId") profileId: String, @Body email: Email): Response<Boolean>

    @POST("edentifica/profiles/insert_phone/{profileId}")
    suspend fun insertPhone(@Path("profileId") profileId: String, @Body phone: Phone): Response<Boolean>

    @POST("edentifica/profiles/insert_social_network/{profileId}")
    suspend fun insertSocialNetwork(@Path("profileId") profileId: String, @Body socialNetwork: SocialNetwork): Response<Boolean>

}