package com.app.edentifica.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.edentifica.ui.screens.ForgotPasswordScreen
import com.app.edentifica.ui.screens.HomeScreen
import com.app.edentifica.ui.screens.LoginScreen
import com.app.edentifica.ui.screens.RegisterScreen
import com.app.edentifica.utils.AuthManager
import com.google.firebase.auth.FirebaseUser

@Composable
fun AppNavigation(context: Context/*repositorio: UsuarioRepositorio,repositorioPizza: PizzaRepositorio*/) {
    //Aqui se maneja toda la navegacion entre nuestras pantallas
    //This handles all the navigation between our screens.
    val navController = rememberNavController()
    //Aqui instancio Auth manager para el ingreso a la aplicacion como anonimo
    //Here I instantiate Auth manager to login to the application as anonymous
    val authManager: AuthManager = AuthManager(context)

    //Aqui recojo al usuario actual que todavia no ha cerrado sesion o que en su defecto es null si no hay usuario con la sesion abierta.
    //Here I get the current user who is not logged out yet or null if there is no user logged in.
    val user: FirebaseUser? = authManager.getCurrentUser()

    NavHost(
        navController = navController,
        startDestination = if(user==null) AppScreen.LoginScreen.route else AppScreen.HomeScreen.route // here we have a conditional, this redirect to screen start depends of user exist or not
    ){
        composable(route=AppScreen.LoginScreen.route){
            LoginScreen(
                navController = navController,
                auth= authManager
            /*LoginViewModel(repositorio)*/
            )
        }

        composable(route=AppScreen.RegisterScreen.route){
            RegisterScreen(
                navController = navController,
                authManager
            /*AltaUsuarioViewModel(repositorio)*/
            )
        }

        composable(route=AppScreen.ForgotPasswordScreen.route){
            ForgotPasswordScreen(
                navController = navController,
                auth = authManager
                /*AltaUsuarioViewModel(repositorio)*/
                )
        }

        composable(route=AppScreen.HomeScreen.route){
            HomeScreen(navController = navController,
                auth= authManager
            /*AltaUsuarioViewModel(repositorio)*/
            )
        }
    }

}