package com.app.edentifica.data.retrofit

import com.app.edentifica.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("edentifica/users/get_by_email/{email}")
    suspend fun getByEmail(@Path("email") email: String): Response<User>

    @GET("edentifica/users/get_by_phone/{phonenumber}")
    suspend fun getByPhone(@Path("phonenumber") phonenumber: String): Response<User>

    @GET("edentifica/users/get_by_type_and_social_network/{type}/{socialname}")
    suspend fun getBySocialNetwork(@Path("type") type: String, @Path("socialname") socialname: String): Response<User>

    @PUT("edentifica/users/update")
    suspend fun updateUser(@Body user: User): Response<Boolean>
    @POST("edentifica/users/insert")
    suspend fun insertUser(@Body user: User): Response<User>

    @POST("edentifica/users/validation_one_call")
    suspend fun toDoCall(@Body user: User): Response<Boolean>

    @POST("edentifica/users/answer_math_challenge")
    suspend fun answerMathChallenge(
        @Query("answer") answer: Int,
        @Body user: User)
    : Response<Boolean>

}