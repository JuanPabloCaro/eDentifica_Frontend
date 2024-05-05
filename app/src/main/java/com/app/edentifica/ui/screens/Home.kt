package com.app.edentifica.ui.screens

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.utils.AuthManager


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, auth : AuthManager/*altaUsuarioViewModel: AltaUsuarioViewModel*/) {
    //VARIABLES Y CONSTANTES
    var menuExpanded by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    val onLogoutConfirmed:()->Unit = {
        auth.signOut()
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
                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ){
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "eDentifica", fontSize = 26.sp, fontStyle = FontStyle.Italic, color = Color.Gray)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Home", fontSize = 18.sp, fontStyle = FontStyle.Italic, color = Color.Gray)
                    }

                },
                actions = {
                    //boton de accion para salir cerrar sesion
                    IconButton(
                        onClick = {
                            showDialog = true
                        }
                    ) {
                        Icon(Icons.Outlined.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )

        },
        bottomBar = {
            BottomAppBar {
                Text(
                    text = "Version 1.0 @Copyrigth 2024 Todos los derechos reservados",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.DarkGray
                )
            }
        }
    ) {
        //funcion para mostrar un pop up preguntando si quiere cerrar la sesion
        contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            if (showDialog) {
                LogoutDialog(onConfirmLogout = {
                    onLogoutConfirmed()
                    showDialog = false
                }, onDismiss = { showDialog = false })
            }

        }
        //funcion composable que pinta el contenido de home
        BodyContentHome(navController)
    }
}

@Composable
fun BodyContentHome(navController: NavController) {
 //Poner el contenido de home
}


/**
 * Funcion composable que se encarga de mostrar un alert para preguntar al
 * usuario si quiere continuar o cerrar sesion
 */
@Composable
fun LogoutDialog(onConfirmLogout: () -> Unit, onDismiss: () -> Unit) {
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
