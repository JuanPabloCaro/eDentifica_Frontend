package com.app.edentifica.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

/**
 * Esta clase se encarga de administrar la autenticacion e ingreso a nuestra aplicacion.
 *
 * This class is responsible for managing authentication and login to our application.
 */

//Aqui vamos a contener los casos de exitos o de error
//Here we will contain the success or error cases
sealed class AuthRes<out T>{
    data class Succes<T>(val data: T ): AuthRes<T>();
    data class Error(val errorMessage: String ): AuthRes<Nothing>();
}
class AuthManager {
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    suspend fun signInAnonymously(): AuthRes<FirebaseUser>{
        return try {
            val result = auth.signInAnonymously().await()
            AuthRes.Succes(result.user ?: throw Exception("Error logging in"))
        }catch (e: Exception){
            AuthRes.Error(e.message ?: "Error logging in")
        }
    }

}