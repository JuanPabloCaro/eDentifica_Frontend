package com.app.edentifica.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.edentifica.ui.screens.Search.FindByEmailScreen
import com.app.edentifica.ui.screens.Search.FindByPhoneScreen
import com.app.edentifica.ui.screens.LoginAndRegister.ForgotPasswordScreen
import com.app.edentifica.ui.screens.HomeScreen
import com.app.edentifica.ui.screens.LoginAndRegister.LoginScreen
import com.app.edentifica.ui.screens.ProfileUser.ProfileScreen
import com.app.edentifica.ui.screens.LoginAndRegister.RegisterPhoneScreen
import com.app.edentifica.ui.screens.LoginAndRegister.RegisterScreen
import com.app.edentifica.ui.screens.ProfileUser.EmailsScreen
import com.app.edentifica.ui.screens.ProfileUser.add.EmailsAddScreen
import com.app.edentifica.ui.screens.ProfileUser.edit.EmailsEditScreen
import com.app.edentifica.ui.screens.Results.ResultSearchEmailScreen
import com.app.edentifica.ui.screens.Results.ResultSearchPhoneScreen
import com.app.edentifica.ui.screens.Results.ResultSearchSocialScreen
import com.app.edentifica.ui.screens.Search.FindBySocialNetworkScreen
import com.app.edentifica.ui.screens.Validations.ValidationOneCheckScreen
import com.app.edentifica.ui.screens.Validations.ValidationOneScreen
import com.app.edentifica.viewModel.UsersViewModel
import com.app.edentifica.utils.AuthManager
import com.google.firebase.auth.FirebaseUser
import com.app.edentifica.utils.googleAuth.SignInState
import com.app.edentifica.viewModel.EmailViewModel
import com.app.edentifica.viewModel.PhonesViewModel
import com.app.edentifica.viewModel.ProfileViewModel


@Composable
fun AppNavigation(
    state: SignInState,
    onSignInClickGoogle: () -> Unit,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
    vmPhones: PhonesViewModel,
    vmEmails: EmailViewModel,
    vmProfiles: ProfileViewModel
) {
    //Aqui se maneja toda la navegacion entre nuestras pantallas
    //This handles all the navigation between our screens.
    val navController = rememberNavController()
    //Aqui instancio Auth manager para el ingreso a la aplicacion como anonimo
    //Here I instantiate Auth manager to login to the application as anonymous
    val authManager: AuthManager = AuthManager()

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
                auth= authManager,
                state = state,
                onSignInClick = onSignInClickGoogle,
                vmUsers=vmUsers
            )
        }

        composable(route=AppScreen.RegisterScreen.route){
            RegisterScreen(
                navController = navController,
                auth = authManager,
                vmUsers=vmUsers
            )
        }

        composable(route=AppScreen.ForgotPasswordScreen.route){
            ForgotPasswordScreen(
                navController = navController,
                auth = authManager
                )
        }

        composable(route= AppScreen.HomeScreen.route){
            HomeScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers= vmUsers
            )
        }

        composable(route=AppScreen.ValidationOneScreen.route){
            ValidationOneScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers=vmUsers
            )
        }

        composable(route=AppScreen.ValidationOneCheckScreen.route){
            ValidationOneCheckScreen(
                navController = navController,
                auth= authManager,
                vmUsers=vmUsers
            )
        }

        composable(route=AppScreen.RegisterPhoneScreen.route){
            RegisterPhoneScreen(
                navController = navController,
                auth= authManager,
                vmUsers=vmUsers,
                vmPhones=vmPhones
            )
        }

        composable(route=AppScreen.ProfileUserScreen.route){
            ProfileScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers= vmUsers
            )
        }

        composable(route=AppScreen.FindByEmailScreen.route){
            FindByEmailScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers= vmUsers
            )
        }

        composable(route=AppScreen.FindByPhoneScreen.route){
            FindByPhoneScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers= vmUsers
            )
        }

        composable(route=AppScreen.FindBySocialNetworkScreen.route){
            FindBySocialNetworkScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers= vmUsers
            )
        }

        composable(route=AppScreen.ResultSearchPhoneScreen.route){
            ResultSearchPhoneScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers= vmUsers
            )
        }

        composable(route=AppScreen.ResultSearchEmailScreen.route){
            ResultSearchEmailScreen(
                navController = navController,
                auth= authManager,
                onSignOutGoogle= onSignOutGoogle,
                vmUsers= vmUsers
            )
        }

        composable(route=AppScreen.ResultSearchSocialScreen.route){
            ResultSearchSocialScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers
            )
        }

        composable(route=AppScreen.EmailsScreen.route){
            EmailsScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers,
                vmEmails=vmEmails
            )
        }

        composable(route=AppScreen.EmailsEditScreen.route){
            EmailsEditScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers,
                vmEmails=vmEmails
            )
        }

        composable(route=AppScreen.EmailsAddScreen.route){
            EmailsAddScreen(
                navController = navController,
                auth = authManager,
                onSignOutGoogle = onSignOutGoogle,
                vmUsers = vmUsers,
                vmEmails=vmEmails,
                vmProfiles=vmProfiles
            )
        }
    }
}