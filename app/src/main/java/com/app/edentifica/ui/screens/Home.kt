package com.app.edentifica.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, /*altaUsuarioViewModel: AltaUsuarioViewModel*/){

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
                Text(text = "Home", fontSize = 26.sp, fontStyle = FontStyle.Italic, color= Color.Gray)
            }
        )
    },
        bottomBar = {
            BottomAppBar {
                Text(text = "Version 1.0 @Copyrigth 2024 Todos los derechos reservados",fontSize = 12.sp, fontStyle = FontStyle.Italic, color= Color.DarkGray)
            }
        }
    ) {
        BodyContentHome(navController, /*altaUsuarioViewModel*/)
    }
}

@Composable
fun BodyContentHome(navController: NavController) {

}
