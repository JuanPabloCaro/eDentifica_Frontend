package com.app.edentifica.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
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


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController, /*altaUsuarioViewModel: AltaUsuarioViewModel*/) {
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Home", fontSize = 26.sp, fontStyle = FontStyle.Italic, color = Color.Gray)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "ArrowBack")
                    }
                },
                actions = {
                    IconButton(onClick = { menuExpanded = !menuExpanded }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
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
        BodyContentHome(navController, menuExpanded) {
            menuExpanded = false // Cerrar el menú al hacer clic fuera de él
        }
    }
}

@Composable
fun BodyContentHome(navController: NavController, menuExpanded: Boolean, onMenuDismiss: () -> Unit) {
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { onMenuDismiss.invoke() },
        modifier = Modifier.padding(end = 16.dp)
    ) {
        // Opciones del menú
        DropdownMenuItem(onClick = { /* Lógica para cerrar sesión */ },
            interactionSource = remember { MutableInteractionSource() }
        ) {
            Text("Cerrar sesión")
        }
    }
}
