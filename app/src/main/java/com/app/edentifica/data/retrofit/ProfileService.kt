package com.app.edentifica.data.retrofit

import androidx.lifecycle.ViewModel
import com.app.edentifica.data.model.Email
import com.app.edentifica.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileService {
    @POST("edentifica/profiles/insert_email/{profileId}")
    suspend fun insertEmail(@Path("profileId") profileId: String, @Body email: Email): Response<Boolean>

}