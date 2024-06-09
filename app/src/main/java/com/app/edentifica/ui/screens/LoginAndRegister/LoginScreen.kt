package com.app.edentifica.ui.screens.LoginAndRegister

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.edentifica.R
import com.app.edentifica.navigation.AppScreen
import com.app.edentifica.ui.theme.AppColors
import com.app.edentifica.ui.theme.TextSizes
import com.app.edentifica.utils.AuthManager
import com.app.edentifica.utils.AuthRes
import com.app.edentifica.utils.googleAuth.SignInState
import com.app.edentifica.viewModel.UsersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navController: NavController,
    auth: AuthManager,
    state: SignInState,
    onSignInClick: () -> Unit,
    vmUsers: UsersViewModel
){
    //VARIABLES Y CONSTANTES
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    //Esta funcion muestra cuando hay un error en el inicio de sesion de google
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let{error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold (
        bottomBar = {
            BottomAppBar(
                containerColor = AppColors.mainEdentifica,
                modifier = Modifier.height(44.dp)
            ) {
                Text(
                    text = stringResource(R.string.copyrigth),
                    fontSize = TextSizes.Footer,
                    fontStyle = FontStyle.Italic,
                    color= AppColors.whitePerlaEdentifica,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.whitePerlaEdentifica) //Color de fondo de la aplicacion
                .padding(8.dp)
        ){
            BodyContent(navController,scope,auth,context,onSignInClick, vmUsers)
        }
    }
}

@Composable
fun BodyContent(
    navController: NavController,
    scope: CoroutineScope,
    auth: AuthManager,
    context: Context,
    onSignInClick: () -> Unit,
    vmUsers: UsersViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        FormularioLogin(Modifier.align(Alignment.Center), navController,scope,auth,context,onSignInClick, vmUsers)
    }
}

@Composable
fun FormularioLogin(
    align: Modifier,
    navController: NavController,
    scope: CoroutineScope,
    auth: AuthManager,
    context: Context,
    onSignInClick: () -> Unit,
    vmUsers: UsersViewModel
) {
    //VARIABLES Y CONSTANTES
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

        // Campo de email - Field email
        Spacer(modifier = Modifier.height(34.dp))
        TextField(
            label = { Text(text = stringResource(R.string.correo), fontSize = TextSizes.Paragraph) },
            value = email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it },
        )

        // Campo de password - Field password
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            label = { Text(text = stringResource(R.string.password),fontSize = TextSizes.Paragraph) },
            value = password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password = it })

        // Botón de inicio de sesión - Login Button
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        emailPassSignIn(email, password, auth, context, navController, vmUsers)
                    }
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.FocusEdentifica),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(R.string.iniciar_sesion),
                    fontSize = TextSizes.H3,
                    color = AppColors.whitePerlaEdentifica)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace de "¿Olvidaste tu contraseña?"
        ClickableText(
            text = AnnotatedString(stringResource(R.string.has_olvidado_tu_contrasena)),
            modifier = Modifier.padding(vertical = 8.dp),
            onClick = {
                navController.navigate(route = AppScreen.ForgotPasswordScreen.route)
            },
            style = TextStyle(
                fontSize = TextSizes.Paragraph,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = AppColors.secondaryEdentifica
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Separador con texto "ó"
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = AppColors.darkEdentifica,
                thickness = 1.dp
            )
            Text(
                text = "ó",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = TextSizes.Paragraph
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = AppColors.darkEdentifica,
                thickness = 1.dp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de inicio de sesión con Google
        SocialMediaButton(
            onClick = onSignInClick,
            text = stringResource(R.string.continuar_con_google),
            icon = R.drawable.ic_google,
            color = Color(0xFFF1F1F1)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de inicio de sesión como invitado
        SocialMediaButton(
            onClick = {
                scope.launch{
                    incognitoSignIn(auth, context, navController)
                }
            },
            text = stringResource(R.string.continuar_como_invitado),
            icon = R.drawable.ic_incognito,
            color = AppColors.grayEdentifica
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Enlace de "¿No tienes una cuenta? Regístrate aquí"
        ClickableText(
            text = AnnotatedString(stringResource(R.string.no_tienes_una_cuenta)),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            onClick= {
                navController.navigate(route = AppScreen.RegisterScreen.route)
            },
            style = TextStyle(
                fontSize = TextSizes.Paragraph,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = AppColors.secondaryEdentifica
            )
        )
    }
}

//Function to login with email and password
private suspend fun emailPassSignIn(
    email: String,
    password: String,
    auth: AuthManager,
    context: Context,
    navController: NavController,
    vmUsers: UsersViewModel
) {
    if(email.isNotEmpty() && password.isNotEmpty()) {
        when (val result = auth.signInWithEmailandPassword(email, password)) {
            is AuthRes.Succes-> {
                navController.navigate(AppScreen.HomeScreen.route) {
                    popUpTo(AppScreen.LoginScreen.route) {
                        inclusive = true
                    }
                }

            }

            is AuthRes.Error -> {
                Toast.makeText(context, "Error Login: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        Toast.makeText(
            context,
            "There are empty fields",
            Toast.LENGTH_SHORT
        ).show()
    }
}

//Function to login how guest
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SocialMediaButton(onClick: () -> Unit, text: String, icon: Int, color: Color) {
    var click by remember { mutableStateOf(false) }
    Surface(
        onClick = onClick,
        modifier = Modifier
            .padding(start = 40.dp, end = 40.dp)
            .clickable { click = !click },
        shape = RoundedCornerShape(50),
        border = BorderStroke(width = 1.dp, color = if(icon == R.drawable.ic_incognito) color else Color.Gray),
        color = color
    ) {
        Row(
            modifier = Modifier
                .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                modifier = Modifier.size(24.dp),
                contentDescription = text,
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "$text", color = if(icon == R.drawable.ic_incognito) Color.White else Color.Black)
            click = true
        }
    }
}

