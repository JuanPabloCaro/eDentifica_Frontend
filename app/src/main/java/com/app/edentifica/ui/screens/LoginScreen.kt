package com.app.edentifica.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.edentifica.R
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.utils.AuthRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, auth: AuthManager/*loginViewModel: LoginViewModel*/){
    //variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold (
        bottomBar = {
            BottomAppBar (){
                Text(text = "Version 1.0 @Copyrigth 2024 Todos los derechos reservados",fontSize = 12.sp, fontStyle = FontStyle.Italic, color= Color.DarkGray)
            }
        }
    ){
        BodyContent(navController, email, password,scope,auth,context/*loginViewModel*/)
    }
}

@Composable
fun BodyContent(
    navController: NavController,
    email: String,
    password: String,
    scope: CoroutineScope,
    auth: AuthManager,
    context: Context
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        FormularioLogin(Modifier.align(Alignment.Center), navController, email,password,scope,auth,context/* loginViewModel*/)
    }
}

@Composable
fun FormularioLogin(
    align: Modifier,
    navController: NavController,
    email: String,
    password: String,
    scope: CoroutineScope,
    auth: AuthManager,
    context: Context
) {
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
            onClick = {
                      scope.launch {
                          incognitoSignIn(auth,context, navController)
                      }
            },
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

private suspend fun incognitoSignIn(auth: AuthManager, context: Context, navController: NavController) {
    when(val result = auth.signInAnonymously()){
        is AuthRes.Succes ->{
            navController.navigate(AppScreen.HomeScreen.route){
                popUpTo(AppScreen.LoginScreen.route){
                    inclusive= true
                }
            }
        }
        is AuthRes.Error ->{
            Log.d("Error modo invitado","Este error se produce cuando un usuario quiere acceder como invitado en la app")
        }
    }
}

