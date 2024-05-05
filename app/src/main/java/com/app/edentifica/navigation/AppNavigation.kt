package com.app.edentifica.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.edentifica.ui.screens.ForgotPasswordScreen
import com.app.edentifica.ui.screens.HomeScreen
import com.app.edentifica.ui.screens.LoginScreen
import com.app.edentifica.ui.screens.RegisterScreen
import com.app.edentifica.utils.AuthManager

@Composable
fun AppNavigation(/*repositorio: UsuarioRepositorio,repositorioPizza: PizzaRepositorio*/) {
    //Aqui se maneja toda la navegacion entre nuestras pantallas
    //This handles all the navigation between our screens.
    val navController = rememberNavController()
    //Aqui instancio Auth manager para el ingreso a la aplicacion como anonimo
    //Here I instantiate Auth manager to login to the application as anonymous
    val authManager: AuthManager = AuthManager()

    NavHost(navController = navController, startDestination = AppScreen.LoginScreen.route ){
        composable(route=AppScreen.LoginScreen.route){
            LoginScreen(navController = navController, auth= authManager /*LoginViewModel(repositorio)*/)
        }

        composable(route=AppScreen.RegisterScreen.route){
            RegisterScreen(navController = navController, /*AltaUsuarioViewModel(repositorio)*/)
        }

        composable(route=AppScreen.ForgotPasswordScreen.route){
            ForgotPasswordScreen(navController = navController, /*AltaUsuarioViewModel(repositorio)*/)
        }

        composable(route=AppScreen.HomeScreen.route){
            HomeScreen(navController = navController, auth= authManager/*AltaUsuarioViewModel(repositorio)*/)
        }
    }

}