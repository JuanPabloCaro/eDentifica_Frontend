package com.app.edentifica.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ForgotPasswordScreen(navController: NavController, /*altaUsuarioViewModel: AltaUsuarioViewModel*/){

    Scaffold(topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(imageVector= Icons.Default.ArrowBack, contentDescription="ArrowBack")
                }
            },
            title = {
                Text(text = "eDentifica", fontSize = 26.sp, fontStyle = FontStyle.Italic, color= Color.Gray)
            }
        )
    },
        bottomBar = {
            BottomAppBar {
                Text(text = "Version 1.0 @Copyrigth 2024 Todos los derechos reservados",fontSize = 12.sp, fontStyle = FontStyle.Italic, color= Color.DarkGray)
            }
        }
    ) {
        BodyContentForgotPassword(navController, /*altaUsuarioViewModel*/)
    }
}

@Composable
fun BodyContentForgotPassword(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        FormularioForgotPassword(Modifier.align(Alignment.Center), navController,/* loginViewModel*/)
    }
}

@Composable
fun FormularioForgotPassword(align: Modifier, navController: NavController) {
    Column(
        modifier = align.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo de correo electrónico - email field
        //var email by remember { mutableStateOf("") }
        OutlinedTextField(
            value = "",
            onValueChange = { /*logica aqui*/},
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para recuperar contraseña - Password recovery button
        Button(
            onClick = {
                // Lógica para recuperar la contraseña
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Password recovery")
        }
    }
}
