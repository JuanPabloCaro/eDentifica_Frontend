package com.app.edentifica.ui.screens.Search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.edentifica.R
import com.app.edentifica.data.model.User
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.viewModel.UsersViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FindByEmailScreen(
    navController: NavController,
    auth: AuthManager,
    onSignOutGoogle: () -> Unit,
    vmUsers: UsersViewModel,
) {
    //VARIABLES Y CONSTANTES
    //para mostrar el dialogo de cerrar Sesion
    var showDialog by remember { mutableStateOf(false) }
    //recojo al user Actual
    val user = auth.getCurrentUser()
    // Llama a getUserByEmail cuando se inicia HomeScreen
    LaunchedEffect(Unit) {
        auth.getCurrentUser()?.email?.let { vmUsers.getUserByEmail(it) }
    }
    // Observa el flujo de usuario en el ViewModel
    val userState by vmUsers.user.collectAsState()

    Log.e("userValidation", userState?.validations?.get(0)?.isValidated.toString())
    Log.e("userValidation", userState?.toString().toString())


    val onLogoutConfirmedFindByEmail:()->Unit = {
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
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if(user?.photoUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user?.photoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen",
                                placeholder = painterResource(id = R.drawable.profile),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(40.dp))
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.profile),
                                contentDescription = "image profile default",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if(!user?.displayName.isNullOrEmpty() || userState!=null) "${userState?.name}" else "Buscar por Correo",//welcomeMessage,
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            (if(!user?.email.isNullOrEmpty()|| userState!=null) "Buscar por correo" else "")?.let {
                                Text(
                                    text = it,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis)
                            }
                        }

                    }
                },
                actions = {
                    //Si el usuario es diferente a null/anonimo, entonces se muestra el boton para navegar al perfil
                    if(auth.getCurrentUser()?.email!=null){
                        // Botón del perfil
                        IconButton(
                            onClick = {
                                navController.navigate(AppScreen.ProfileUserScreen.route)
                            }
                        ) {
                            Icon(Icons.Filled.AccountCircle, contentDescription = "Perfil")
                        }
                    }

                    //boton de accion para salir cerrar sesion
                    IconButton(
                        onClick = {
                            showDialog = true
                        }
                    ) {
                        Icon(Icons.Outlined.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                },
                backgroundColor= Color.Gray
            )
        },
        bottomBar = {
            BottomAppBar (backgroundColor = Color.DarkGray){
                Text(
                    text = "Version 1.0 @Copyrigth 2024 Todos los derechos reservados",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
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
                LogoutDialogFindByEmail(
                    onConfirmLogout = {
                        onLogoutConfirmedFindByEmail()
                        showDialog = false
                    },
                    onDismiss = { showDialog = false })
            }

        }
        //funcion composable que pinta el contenido de home
        BodyContentFindByEmail(navController, vmUsers, userState)
    }
}







@Composable
fun BodyContentFindByEmail(navController: NavController, vmUsers: UsersViewModel, userState: User?) {

    // Estado para almacenar el correo electrónico ingresado por el usuario
    var email by remember { mutableStateOf("") }

    // Estado para controlar si se ha realizado una búsqueda
    var searchPerformedEmail by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Campo de entrada para el correo electrónico
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Botón para enviar la búsqueda
        Button(
            onClick = {
                // Llamar a la función del ViewModel para buscar por correo electrónico
                vmUsers.getUserByEmailSearch(email)
                searchPerformedEmail = true
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Buscar")
        }

        // Mostrar el resultado de la búsqueda o el mensaje de error
        if (searchPerformedEmail) {
            navController.navigate(AppScreen.ResultSearchEmailScreen.route)
        }
    }

}







/**
 * Funcion composable que se encarga de mostrar un alert para preguntar al
 * usuario si quiere continuar o cerrar sesion
 */
@Composable
fun LogoutDialogFindByEmail(
    onConfirmLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar sesión") },
        text = { Text("¿Estás seguro que deseas cerrar sesión?") },
        confirmButton = {
            Button(
                onClick = onConfirmLogout,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
            ) {
                Text("Aceptar",color= Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Text("Cancelar",color= Color.White)
            }
        }
    )
}

