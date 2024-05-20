package com.app.edentifica.navigation

sealed class AppScreen(val route: String){
    //Definimos las pantallas de nuestra aplicacion
    //We define the screens of our application
    object LoginScreen: AppScreen("login_screen")

    object RegisterScreen: AppScreen("register_screen")

    object ForgotPasswordScreen: AppScreen("forgot_password_screen")

    object HomeScreen: AppScreen("home_screen")

    object ValidationOneScreen: AppScreen("validation_one_screen")

    object ValidationOneCheckScreen: AppScreen("validation_one_check_screen")

    object RegisterPhoneScreen: AppScreen("register_phone")

    object ProfileUserScreen: AppScreen("profile_user")

}