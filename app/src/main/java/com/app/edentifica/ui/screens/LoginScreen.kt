package com.app.edentifica.ui.screens

import android.annotation.SuppressLint
import android.service.autofill.OnClickAction
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.edentifica.R
import com.app.edentifica.navigation.AppScreen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, /*loginViewModel: LoginViewModel*/){
    Scaffold (
        bottomBar = {
            BottomAppBar (){
                Text(text = "Version 1.0 @Copyrigth 2024 Todos los derechos reservados",fontSize = 12.sp, fontStyle = FontStyle.Italic, color= Color.DarkGray)
            }
        }
    ){
        BodyContent(navController, /*loginViewModel*/)
    }
}

@Composable
fun BodyContent(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        FormularioLogin(Modifier.align(Alignment.Center), navController,/* loginViewModel*/)
    }
}

@Composable
fun FormularioLogin(align: Modifier, navController: NavController) {
    Column(
        modifier = align.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Logo
        Image(
            painter = painterResource(id = R.drawable.nombre_edentifica),
            contentDescription = "Logo eDentifica",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp), // ajusta la altura según sea necesario
            contentScale = ContentScale.Crop // Escala de la imagen
        )

        //Title
        Text(text = "Login", color = Color.DarkGray, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(32.dp))

        // Campo de email - Field email
        TextField(
            value = "",
            onValueChange = { /* No hacer nada por ahora */ },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { /* Mover el enfoque al siguiente campo si es necesario */ }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña - Field password
        TextField(
            value = "",
            onValueChange = { /* No hacer nada por ahora */ },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { /* Ocultar el teclado si es necesario */ }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de inicio de sesión - Login Button
        Button(
            onClick = { /* Llamar a la función de inicio de sesión con el email y la contraseña */ },
            modifier = Modifier.align(alignment = androidx.compose.ui.Alignment.CenterHorizontally)
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Enlace de "¿Olvidaste tu contraseña?"
        ClickableText(
            text = AnnotatedString("Forgot your password?"),
            modifier = Modifier.padding(vertical = 8.dp),
            onClick = {
                navController.navigate(route = AppScreen.ForgotPasswordScreen.route)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Separador con texto "ó"
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = Color.Black,
                thickness = 1.dp
            )
            Text(
                text = "ó",
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = Color.Black,
                thickness = 1.dp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de inicio de sesión con Google
        Button(
            onClick = { /* Lógica para iniciar sesión con Google */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign in with Google")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de inicio de sesión como invitado
        Button(
            onClick = { /* Lógica para iniciar sesión como invitado */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login as guest")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace de "¿No tienes una cuenta? Regístrate aquí"
        ClickableText(
            text = AnnotatedString("Don't have an account? Sign up here."),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            onClick= {
                navController.navigate(route = AppScreen.RegisterScreen.route)
            },
        )
    }
}

