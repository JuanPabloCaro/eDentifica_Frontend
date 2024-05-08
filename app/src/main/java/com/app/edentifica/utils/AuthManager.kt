package com.app.edentifica.utils


import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.app.edentifica.R
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
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



class AuthManager(private val context: Context) {// Se crea el contexto para el usuario
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val signInClient = Identity.getSignInClient(context)

    //Function to login guest
    suspend fun signInAnonymously(): AuthRes<FirebaseUser>{
        return try {
            val result = auth.signInAnonymously().await()
            AuthRes.Succes(result.user ?: throw Exception("Error logging in"))
        }catch (e: Exception){
            AuthRes.Error(e.message ?: "Error logging in how guest")
        }
    }


    //Funcion para crear usuarios, esto es temporal, se debe de usar retrofit para insertar el usuario
    suspend fun createUserWithEmailandPassword(email:String, password:String): AuthRes<FirebaseUser?>{
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            AuthRes.Succes(authResult.user)
        }catch (e: Exception){
            AuthRes.Error(e.message ?: "Error to create user")
        }
    }


    //Function to login with email and password
    suspend fun signInWithEmailandPassword(email:String, password:String): AuthRes<FirebaseUser?>{
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            AuthRes.Succes(authResult.user)
        }catch (e: Exception){
            AuthRes.Error(e.message ?: "Error loggin in with email and password")
        }
    }


    suspend fun resetPassword(email: String): AuthRes<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthRes.Succes(Unit)
        }catch (e: Exception){
            AuthRes.Error(e.message?: "Error to reset password")
        }
    }


    fun signOut(){
        auth.signOut();
        signInClient.signOut();
    }

    fun getCurrentUser(): FirebaseUser?{
        return auth.currentUser
    }

    //Functions to login with Google
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("694564092160-lsndc0lhnda2ecvg3mc3vu9u4etr9ood.apps.googleusercontent.com")
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }


    fun handleSignInResult(task: Task<GoogleSignInAccount>): AuthRes<GoogleSignInAccount>? {
        return try {
            val account = task.getResult(ApiException::class.java)
            AuthRes.Succes(account)
        } catch (e: ApiException) {
            AuthRes.Error(e.message ?: "Google sign-in failed.")
        }
    }

    suspend fun signInWithGoogleCredential(credential: AuthCredential): AuthRes<FirebaseUser>? {
        return try {
            val firebaseUser = auth.signInWithCredential(credential).await()
            firebaseUser.user?.let {
                AuthRes.Succes(it)
            } ?: throw Exception("Sign in with Google failed.")
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Sign in with Google failed.")
        }
    }

    fun signInWithGoogle(googleSignInLauncher: ActivityResultLauncher<Intent>) {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

}