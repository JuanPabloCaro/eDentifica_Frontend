package com.app.edentifica.ui.screens

import  android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.getValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.edentifica.R
import com.app.edentifica.data.model.Email
import com.app.edentifica.data.model.Phone
import com.app.edentifica.data.model.Profile
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.viewModel.UsersViewModel
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.data.model.User
import com.app.edentifica.ui.screens.Validations.BodyContentValidationOne
import com.app.edentifica.ui.theme.AppColors
import com.app.edentifica.ui.theme.TextSizes
import com.google.firebase.auth.FirebaseUser
import kotlin.coroutines.suspendCoroutine


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
) {
    // VARIABLES Y CONSTANTES
    //para mostrar el dialogo de cerrar Sesion
    var showDialog by remember { mutableStateOf(false) }

    //recojo al user Actual
    val currentUser = auth.getCurrentUser()

    // Llama a getUserByEmail cuando se inicia HomeScreen
    LaunchedEffect(Unit) {
        val userEmail = auth.getCurrentUser()?.email
        if (userEmail != null) {
            vmUsers.getUserByEmail(userEmail)
        }
    }

    // Observa el flujo de usuario en el ViewModel
    val userState by vmUsers.user.collectAsState()

    //si el user es existe le pregunto si ya esta validado
    if(userState != null){
        Log.e("entra 1", "entra en user state")
        if(userState?.validations?.get(0)?.isValidated==false && userState?.validations?.get(1)?.isValidated==false){ // importante modificacion en home
            navController.navigate(AppScreen.InfoValidationsScreen.route){
                popUpTo(AppScreen.HomeScreen.route){
                    inclusive= true
                }
            }
        }else if(userState?.validations?.get(0)?.isValidated==true && userState?.validations?.get(1)?.isValidated==false){
            navController.navigate(AppScreen.ValidationTwoScreen.route){
                popUpTo(AppScreen.HomeScreen.route){
                    inclusive= true
                }
            }
        }
    }else {
        if(currentUser?.email != null) {// si el usuario existe en firebase y no existe en la base de datos lo inserto en la base de datos
            Log.e("entra 2", "entra en user state y currentUser")
            val userToInsert: User = User(
                null,
                null,
                auth.getCurrentUser()?.displayName.toString(),
                "",
                Phone(null,auth.getCurrentUser()?.phoneNumber.toString(),false,null),
                Email(null,auth.getCurrentUser()?.email.toString(),false,null),
                Profile(null,"",auth.getCurrentUser()?.photoUrl.toString(),null),
                null,
                null
            )
            // Llama a la función del ViewModel para insertar el usuario y espera a que se complete
            LaunchedEffect (Unit) {
                Log.e("entra 3", "entra en user state")
                vmUsers.insertUserVm(userToInsert)
                vmUsers.getUserByEmail(auth.getCurrentUser()?.email.toString())
            }
        }

    }


    val onLogoutConfirmed:()->Unit = {
        auth.signOut()
        onSignOutGoogle()

        navController.navigate(AppScreen.LoginScreen.route){
            popUpTo(AppScreen.HomeScreen.route){
                inclusive= true
            }
        }
    }

    //Estructura de la pantalla
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.mainEdentifica),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                            if(auth.getCurrentUser()?.email!=null && userState?.validations?.get(0)?.isValidated ==true ){
                                userState?.profile?.urlImageProfile?.let {
                                    ClickableProfileImage(
                                        navController = navController,
                                        imageUrl = it
                                    ) {
                                        navController.navigate(AppScreen.ProfileUserScreen.route)
                                    }
                                }
                            }

                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if(!currentUser?.displayName.isNullOrEmpty() || userState!=null) "Hola ${userState?.name}" else "Bienvenid@",//welcomeMessage,
                                fontSize = TextSizes.H3,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = AppColors.whitePerlaEdentifica
                            )
                            (if(!currentUser?.email.isNullOrEmpty()|| userState!=null) userState?.email?.email else "Usuario Anonimo")?.let {
                                Text(
                                    text = it,
                                    fontSize = TextSizes.Footer,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = AppColors.whitePerlaEdentifica
                                )
                            }
                        }

                    }
                },
                actions = {
                    //Botton Home
                    IconButton(
                        onClick = {
                            navController.navigate(AppScreen.HomeScreen.route)
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Home,
                            contentDescription = "Home",
                            tint = AppColors.whitePerlaEdentifica
                        )
                    }
                    //boton de accion para salir cerrar sesion
                    IconButton(
                        onClick = {
                            showDialog = true
                        }
                    ) {
                        Icon(
                            Icons.Outlined.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = AppColors.whitePerlaEdentifica
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = AppColors.mainEdentifica,
                modifier = Modifier.height(44.dp)
            ) {
                Text(
                    text = stringResource(R.string.copyrigth),
                    fontSize = TextSizes.Footer,
                    fontStyle = FontStyle.Italic,
                    color = AppColors.whitePerlaEdentifica,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    ) {
        //funcion para mostrar un pop up preguntando si quiere cerrar la sesion
        contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            if (showDialog) {
                LogoutDialog(
                    onConfirmLogout = {
                        onLogoutConfirmed()
                        showDialog = false
                    },
                    onDismiss = { showDialog = false })
            }

        }

        //funcion composable que pinta el contenido de home
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.whitePerlaEdentifica) //Color de fondo de la aplicacion
                .padding(24.dp)
        ){
            BodyContentHome(navController, vmUsers, userState)
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyContentHome(
    navController: NavController,
    vmUsers: UsersViewModel,
    userState: User?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Title
        Text(
            text = "¿Qué quieres buscar?",
            fontSize = TextSizes.H1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        //Image
        Image(
            painter = painterResource(id = R.drawable.searchhome),
            contentDescription = "search",
            modifier = Modifier
                .fillMaxWidth()
                .scale(0.7f)
                .padding(0.dp), // ajusta la altura según sea necesario
            contentScale = ContentScale.Crop // Escala de la imagen
        )


        //Button correo
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    navController.navigate(AppScreen.FindByEmailScreen.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Buscar Correo")
            }
        }

        //Button telefono
        Spacer(modifier = Modifier.height(34.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    navController.navigate(AppScreen.FindByPhoneScreen.route)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Buscar Telefono")
            }
        }

        //Button Red Social
        Spacer(modifier = Modifier.height(34.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    navController.navigate(AppScreen.FindBySocialNetworkScreen.route)                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Buscar Red Social")
            }
        }
    }
}

/**
 * Imagen de perfil clikeable
 */
@Composable
fun ClickableProfileImage(
    navController: NavController,  // Assuming you're using NavController for navigation
    imageUrl: String,
    onClick: () -> Unit  // Define your click action
) {
    if(!imageUrl.equals("")){
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .clickable { onClick() }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen",
                placeholder = painterResource(id = R.drawable.profile),
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )
        }
    }else{
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .clickable { onClick() }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.profile)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen",
                placeholder = painterResource(id = R.drawable.profile),
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )
        }
    }


}

//
///**
// * Function to create a user
// */
//private fun createUserFromAuth(authUser: FirebaseUser?): User {
//    return User(
//        null,
//        null,
//        authUser?.displayName.toString(),
//        "",
//        Phone(null, authUser?.phoneNumber.toString(), false, null),
//        Email(null, authUser?.email.toString(), false, null),
//        Profile(null, "", authUser?.photoUrl.toString(), null),
//        null,
//        null
//    )
//}


/**
 * Funcion composable que se encarga de mostrar un alert para preguntar al
 * usuario si quiere continuar o cerrar sesion
 */
@Composable
fun LogoutDialog(
    onConfirmLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = AppColors.whitePerlaEdentifica,
        onDismissRequest = onDismiss,
        title = { Text("Cerrar sesión", color = AppColors.mainEdentifica) },
        text = { Text("¿Estás seguro que deseas cerrar sesión?",color = AppColors.mainEdentifica) },
        confirmButton = {
            Button(
                onClick = onConfirmLogout,
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica)
            ) {
                Text("Aceptar", color = AppColors.whitePerlaEdentifica)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(1.dp, AppColors.FocusEdentifica),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.FocusEdentifica)
            ) {
                Text("Cancelar")
            }
        }
    )
}
